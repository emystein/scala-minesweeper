package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.Shown

import scala.util.Random

object Board {
  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions, cellStateByCoordinate(dimensions, totalBombs))
  }

  def cellStateByCoordinate(dimensions: Dimensions, totalBombs: Int): Map[CartesianCoordinates, CellState] = {
    val allCoordinates = CartesianCoordinates.all(dimensions.rows, dimensions.columns)

    val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        val coordinates = CartesianCoordinates(row, column)
        val hasBomb = bombCoordinates.contains(coordinates)
        coordinates -> CellState(hasBomb)
      }
    }.toMap
  }
}

case class Board(dimensions: Dimensions,
                 cellsState: Map[CartesianCoordinates, CellState]) extends RectangleCoordinates {

  val cells: Cells = Cells(this)

  def cellAt(coordinates: CartesianCoordinates): Cell = cells.all.filter(_.coordinates == coordinates).head

  def markCell(coordinates: CartesianCoordinates, mark: Option[CellMark]): Board =
    copy(cellsState = cellsState + (coordinates -> cellsState(coordinates).copy(mark = mark)))

  def revealCell(coordinates: CartesianCoordinates): Board =
    copy(cellsState = cellsState + (coordinates -> cellsState(coordinates).copy(visibility = Visibility.Shown)))
}
