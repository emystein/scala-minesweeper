package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.CellContent.Bomb
import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

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
          initializeContent(dimensions, totalBombs),
          hideAll(dimensions),
          fillNoMarkAll(dimensions))
  }

  def initializeContent(dimensions: Dimensions, totalBombs: Int): Map[CartesianCoordinates, CellContent] = {
    val allCoordinates = CartesianCoordinates.all(dimensions.rows, dimensions.columns)

    val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

    allCoordinates.map(coordinates => coordinates -> CellContent(bombCoordinates.contains(coordinates))).toMap
  }

  def hideAll(dimensions: Dimensions): Map[CartesianCoordinates, Visibility] =
    CartesianCoordinates.all(dimensions.rows, dimensions.columns).map(_ -> Visibility.Hidden).toMap

  def fillNoMarkAll(dimensions: Dimensions): Map[CartesianCoordinates, Option[CellMark]] =
    CartesianCoordinates.all(dimensions.rows, dimensions.columns).map(_ -> None).toMap
}

case class Board(dimensions: Dimensions,
                 contentByCoordinates: Map[CartesianCoordinates, CellContent],
                 visibilityByCoordinates: Map[CartesianCoordinates, Visibility],
                 markByCoordinates: Map[CartesianCoordinates, Option[CellMark]],
                ) extends RectangleCoordinates {

  val allCells: Iterable[Cell] = for {
    (coordinates, content) <- contentByCoordinates
    visibility = visibilityByCoordinates(coordinates)
    mark = markByCoordinates(coordinates)
  } yield {
    Cell(coordinates, content, visibility, mark, board = Some(this))
  }
  val emptyCells: Iterable[Cell] = allCells.filter(_.isEmpty)
  val cellsWithBomb: Iterable[Cell] = allCells.filter(_.content == CellContent.Bomb)
  val hiddenCells = allCells.filter(_.isHidden)
  val revealedCells = allCells.filter(_.isRevealed)

  val hasAllEmptyCellsRevealed: Boolean = revealedCells.toSet == emptyCells.toSet

  val hasACellWithBombRevealed: Boolean = revealedCells.exists(_.content == Bomb)

  def cellAt(coordinates: CartesianCoordinates): Cell = allCells.filter(_.coordinates == coordinates).head

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
