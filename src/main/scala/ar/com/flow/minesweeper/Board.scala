package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

import scala.util.Random

case class CellState(content: Option[CellContent] = None,
                     visibility: Visibility = Hidden,
                     mark: Option[String] = None)

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
        coordinates -> CellState(CellContent(hasBomb))
      }
    }.toMap
  }
}

case class Board(dimensions: Dimensions,
                 cellsState: Map[CartesianCoordinates, CellState]) extends RectangleCoordinates {

  val cellAt: Map[CartesianCoordinates, Cell] = cellsState.map {
    case (coordinates, state) => coordinates -> Cell(coordinates, state, this)
  }

  val cells: Cells = Cells(cellAt.values)

  def setCellValue(coordinates: CartesianCoordinates, value: Option[String]): Board =
    copy(cellsState = cellsState + (coordinates -> cellsState(coordinates).copy(mark = value)))

  def revealCell(coordinates: CartesianCoordinates): Board =
    copy(cellsState = cellsState + (coordinates -> cellsState(coordinates).copy(visibility = Shown)))
}
