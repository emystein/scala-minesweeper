package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Board.Coordinates

import scala.collection.mutable
import scala.util.Random

object Board {
  type Coordinates = (Int, Int)

  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions, totalBombs, cellsByCoordinates(dimensions, totalBombs))
  }

  def cellsByCoordinates(dimensions: Dimensions, totalBombs: Int): mutable.Map[Coordinates, Cell] = {
    val cellLocationContext = new CellLocationContext(dimensions.rows, dimensions.columns)

    val cells = {
      val allCoordinates = this.allCoordinates(dimensions.rows, dimensions.columns)

      val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        val hasBomb = bombCoordinates.contains(row, column)
        val adjacentBombs = cellLocationContext.neighboursOf(row, column).count(bombCoordinates.contains)
        Cell(row, column, hasBomb, adjacentBombs)
      }
    }

    mutable.HashMap(cells.map(c => (c.row, c.column) -> c): _*)
  }

  def allCoordinates(totalRows: Int, totalColumns: Int): Seq[Coordinates] = {
    for {
      row <- 1 to totalRows
      column <- 1 to totalColumns
    } yield {
      (row, column)
    }
  }
}

case class Board(dimensions: Dimensions, totalBombs: Int, cellsByCoordinates: mutable.Map[Coordinates, Cell]) {
  val cellLocationContext = new CellLocationContext(dimensions.rows, dimensions.columns)

  def cells = Cells(cellsByCoordinates.values.toSet)

  def getCell(coordinates: Coordinates): Cell = {
    cellsByCoordinates(coordinates)
  }

  def setCellValue(coordinates: Coordinates, value: String): Board = {
    cellsByCoordinates(coordinates) = cellsByCoordinates(coordinates).copy(value = value)
    Board(dimensions, totalBombs, cellsByCoordinates)
  }

  def revealCell(cell: Cell): Board = {
    revealCell(cell.row, cell.column)
  }

  def revealCell(coordinates: Coordinates): Board = {
    cellsByCoordinates(coordinates) = cellsByCoordinates(coordinates).copy(isRevealed = true)
    Board(dimensions, totalBombs, cellsByCoordinates)
  }

  def adjacentCellsOf(cell: Cell): Seq[Cell] = {
    cellLocationContext.neighboursOf(cell).map(getCell)
  }
}
