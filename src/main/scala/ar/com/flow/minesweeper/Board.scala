package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

import scala.util.Random

case class CellState(content: Option[CellContent] = None,
                     visibility: Visibility = Hidden,
                     mark: Option[String] = None)

object Board {
  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions, totalBombs, cellStateByCoordinate(dimensions, totalBombs))
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

case class Board(dimensions: Dimensions, totalBombs: Int, cellStateByCoordinate: Map[CartesianCoordinates, CellState]) extends RectangleCoordinates {
  val cellsByCoordinate: Map[CartesianCoordinates, Cell] = cellStateByCoordinate.map((c: (CartesianCoordinates, CellState)) => c._1 -> Cell(c._1, c._2, this))

  val cells: Cells = Cells(cellsByCoordinate.values.toSet)

  def cellAt(coordinates: CartesianCoordinates): Cell = {
    cellsByCoordinate(coordinates)
  }

  def cellsAdjacentTo(coordinates: CartesianCoordinates): Seq[Cell] = adjacentOf(coordinates).map(cellAt)

  def setCellValue(coordinates: CartesianCoordinates, value: Option[String]): Board = {
    copy(cellStateByCoordinate = cellStateByCoordinate + (coordinates -> cellStateByCoordinate(coordinates).copy(mark = value)))
  }

  def revealCell(coordinates: CartesianCoordinates): Board = {
    copy(cellStateByCoordinate = cellStateByCoordinate + (coordinates -> cellStateByCoordinate(coordinates).copy(visibility = Shown)))
  }
}
