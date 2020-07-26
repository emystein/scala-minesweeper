package ar.com.flow.minesweeper

import scala.util.Random

object Board {
  def apply(dimensions: Dimensions, cellsByCoordinates: Map[CartesianCoordinates, Cell]): Board = {
    // TODO improve
    val contentByCoordinates = cellsByCoordinates.map{ case (coordinates, cell) => coordinates -> cell.content }
    val visibilityByCoordinates = cellsByCoordinates.map{ case (coordinates, cell) => coordinates -> cell.visibility }
    val markByCoordinates = cellsByCoordinates.map{ case (coordinates, cell) => coordinates -> cell.mark }
    Board(dimensions, contentByCoordinates, visibilityByCoordinates, markByCoordinates)
  }

  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions,
          contentByCoordinates(dimensions, totalBombs),
          visibilityByCoordinates(dimensions),
          markByCoordinates(dimensions))
  }

  def contentByCoordinates(dimensions: Dimensions, totalBombs: Int): Map[CartesianCoordinates, CellContent] = {
    val allCoordinates = CartesianCoordinates.all(dimensions.rows, dimensions.columns)

    val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        val coordinates = CartesianCoordinates(row, column)
        val hasBomb = bombCoordinates.contains(coordinates)
        coordinates -> CellContent(hasBomb)
      }
    }.toMap
  }

  def visibilityByCoordinates(dimensions: Dimensions): Map[CartesianCoordinates, Visibility] = {
    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        CartesianCoordinates(row, column) -> Visibility.Hidden
      }
    }.toMap
  }

  def markByCoordinates(dimensions: Dimensions): Map[CartesianCoordinates, Option[CellMark]] = {
    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        CartesianCoordinates(row, column) -> None
      }
    }.toMap
  }
}

case class Board(dimensions: Dimensions,
                 contentByCoordinates: Map[CartesianCoordinates, CellContent],
                 visibilityByCoordinates: Map[CartesianCoordinates, Visibility],
                 markByCoordinates: Map[CartesianCoordinates, Option[CellMark]],
                ) extends RectangleCoordinates {

  val cells: Cells = Cells(contentByCoordinates, visibilityByCoordinates, markByCoordinates, this)

  def cellAt(coordinates: CartesianCoordinates): Cell = cells.all.filter(_.coordinates == coordinates).head

  def toggleMarkAt(coordinates: CartesianCoordinates): Board =
    copy(markByCoordinates = markByCoordinates + (coordinates -> cellAt(coordinates).advanceMark.mark))

  def revealCellAt(coordinates: CartesianCoordinates): Board =
    copy(visibilityByCoordinates = visibilityByCoordinates + (coordinates -> Visibility.Shown))

  def revealCellAndAdjacentAt(coordinates: CartesianCoordinates): Board = {
    val revealedCellBoard = revealCellAt(coordinates)

    revealedCellBoard.cellAt(coordinates).adjacentEmptySpace()
      .foldLeft(revealedCellBoard)((board, cell) => board.revealCellAt(cell.coordinates))
  }
}
