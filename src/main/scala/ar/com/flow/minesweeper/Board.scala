package ar.com.flow.minesweeper

import scala.util.Random

object Board {
  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions, cellsByCoordinates(dimensions, totalBombs))
  }

  def cellsByCoordinates(dimensions: Dimensions, totalBombs: Int): Map[CartesianCoordinates, Cell] = {
    val allCoordinates = CartesianCoordinates.all(dimensions.rows, dimensions.columns)

    val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        val coordinates = CartesianCoordinates(row, column)
        val hasBomb = bombCoordinates.contains(coordinates)
        coordinates -> Cell(coordinates, hasBomb)
      }
    }.toMap
  }
}

case class Board(dimensions: Dimensions,
                 cellsByCoordinates: Map[CartesianCoordinates, Cell]) extends RectangleCoordinates {

  val cells: Cells = Cells(cellsByCoordinates.values)

  def cellAt(coordinates: CartesianCoordinates): Cell = cells.all.filter(_.coordinates == coordinates).head

  def adjacentCells(cell: Cell): Set[Cell] = adjacentOf(cell.coordinates).map(cellAt)

  def adjacentEmptySpace(cell: Cell, previouslyTraversed: Set[Cell] = Set.empty): Set[Cell] = {
    (adjacentCells(cell) -- previouslyTraversed)
      .filter(_.content == CellContent.Empty)
      .foldLeft(previouslyTraversed + cell)((traversed, adjacent) => adjacentEmptySpace(adjacent, traversed))
  }

  def markCellAt(coordinates: CartesianCoordinates, mark: Option[CellMark]): Board =
    copy(cellsByCoordinates = cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(mark = mark)))

  def revealCellAt(coordinates: CartesianCoordinates): Board =
    copy(cellsByCoordinates = cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(visibility = Visibility.Shown)))
}
