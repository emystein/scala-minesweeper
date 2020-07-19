package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.Shown

import scala.util.Random

case class CellData(coordinates: CartesianCoordinates, content: Option[CellContent] = None, visibility: Visibility = Visibility.Hidden, mark: Option[String] = None)

object Board {
  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions, totalBombs, cellsByCoordinates(dimensions, totalBombs))
  }

  def cellsByCoordinates(dimensions: Dimensions, totalBombs: Int): Map[CartesianCoordinates, CellData] = {
    val allCoordinates = this.allCoordinates(dimensions.rows, dimensions.columns)

    val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        val coordinates = CartesianCoordinates(row, column)
        val hasBomb = bombCoordinates.contains(coordinates)
        coordinates -> CellData(coordinates, CellContent(hasBomb))
      }
    }.toMap
  }

  def allCoordinates(totalRows: Int, totalColumns: Int): Seq[CartesianCoordinates] = {
    for {
      row <- 1 to totalRows
      column <- 1 to totalColumns
    } yield {
      CartesianCoordinates(row, column)
    }
  }
}

case class Board(dimensions: Dimensions, totalBombs: Int, cellsByCoordinates: Map[CartesianCoordinates, CellData]) {
  def cells: Cells = Cells(cellsByCoordinates.values.toSet.map((c: CellData) => Cell(c, this)))

  def cellAt(coordinates: CartesianCoordinates): Cell = {
    Cell(cellsByCoordinates(coordinates), this)
  }

  def setCellValue(coordinates: CartesianCoordinates, value: Option[String]): Board = {
    Board(dimensions, totalBombs, cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(mark = value)))
  }

  def revealCell(coordinates: CartesianCoordinates): Board = {
    copy(cellsByCoordinates = cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(visibility = Shown)))
  }
}
