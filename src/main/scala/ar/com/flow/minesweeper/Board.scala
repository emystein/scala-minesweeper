package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

import scala.util.Random

case class CellData(content: Option[CellContent] = None,
                    visibility: Visibility = Hidden,
                    mark: Option[String] = None)

object Board {
  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions, totalBombs, cellsByCoordinates(dimensions, totalBombs))
  }

  def cellsByCoordinates(dimensions: Dimensions, totalBombs: Int): Map[CartesianCoordinates, CellData] = {
    val allCoordinates = CartesianCoordinates.all(dimensions.rows, dimensions.columns)

    val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        val coordinates = CartesianCoordinates(row, column)
        val hasBomb = bombCoordinates.contains(coordinates)
        coordinates -> CellData(CellContent(hasBomb))
      }
    }.toMap
  }
}

case class Board(dimensions: Dimensions, totalBombs: Int, cellData: Map[CartesianCoordinates, CellData]) {
  def cells: Cells =
    Cells(cellData.toSet.map((c: (CartesianCoordinates, CellData)) => Cell(c._1, c._2, this)))

  def cellAt(coordinates: CartesianCoordinates): Cell = {
    Cell(coordinates, cellData(coordinates), this)
  }

  def setCellValue(coordinates: CartesianCoordinates, value: Option[String]): Board = {
    copy(cellData = cellData + (coordinates -> cellData(coordinates).copy(mark = value)))
  }

  def revealCell(coordinates: CartesianCoordinates): Board = {
    copy(cellData = cellData + (coordinates -> cellData(coordinates).copy(visibility = Shown)))
  }
}
