package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Board.Coordinates

import scala.util.Random

object Board {
  type Coordinates = (Int, Int)

  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions, totalBombs, cellsByCoordinates(dimensions, totalBombs))
  }

  def cellsByCoordinates(dimensions: Dimensions, totalBombs: Int): Map[Coordinates, Cell] = {
    val allCoordinates = this.allCoordinates(dimensions.rows, dimensions.columns)

    val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        val hasBomb = bombCoordinates.contains(row, column)
        (row, column) -> Cell(row, column, hasBomb)
      }
    }.toMap
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

case class Board(dimensions: Dimensions, totalBombs: Int, cellsByCoordinates: Map[Coordinates, Cell]) {
  val cellLocationContext = new CellLocationContext(dimensions)

  def cells = Cells(cellsByCoordinates.values.toSet)

  def getCell(coordinates: Coordinates): Cell = {
    cellsByCoordinates(coordinates)
  }

  def setCellValue(coordinates: Coordinates, value: String): Board = {
    Board(dimensions, totalBombs, cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(value = value)))
  }

  def revealCell(cell: Cell): Board = {
    revealCell(cell.row, cell.column)
  }

  def revealCell(coordinates: Coordinates): Board = {
    Board(dimensions, totalBombs, cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(isRevealed = true)))
  }

  def adjacentCellsOf(cell: Cell): Seq[Cell] = {
    cellLocationContext.neighboursOf(cell).map(getCell)
  }

  def adjacentBombsOf(cell: Cell): Seq[Cell] = {
    cellLocationContext.neighboursOf(cell).map(getCell).filter(_.hasBomb)
  }
}
