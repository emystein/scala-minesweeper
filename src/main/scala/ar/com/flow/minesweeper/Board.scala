package ar.com.flow.minesweeper

import scala.collection.mutable
import scala.util.Random
import Board.Coordinates

object Board {
  type Coordinates = (Int, Int)

  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Board = {
    require(totalRows > 0)
    require(totalColumns > 0)
    require(totalBombs >= 0)

    val cellLocationContext = new CellLocationContext(totalRows, totalColumns)

    val cells = {
      val allCoordinates = for {
        rowNumber <- 1 to totalRows
        columnNumber <- 1 to totalColumns
      } yield {
        (rowNumber, columnNumber)
      }

      val randomCoordinates = Random.shuffle(allCoordinates)
      val bombCoordinates = randomCoordinates.take(totalBombs)

      for {
        row <- 1 to totalRows
        column <- 1 to totalColumns
      } yield {
        val hasBomb = bombCoordinates.contains(row, column)
        val adjacentBombs = cellLocationContext.neighboursOf(row, column).count(bombCoordinates.contains)
        Cell(row, column, hasBomb, adjacentBombs)
      }
    }

    val cellMap = mutable.HashMap(cells.map(c => (c.row, c.column) -> c): _*)
    Board(totalRows, totalColumns, totalBombs, cellMap)
  }
}

case class Board(totalRows: Int, totalColumns: Int, totalBombs: Int, cellsByCoordinates: mutable.Map[Coordinates, Cell]) {
  val cellLocationContext = new CellLocationContext(totalRows, totalColumns)

  def cells = cellsByCoordinates.values.toSeq

  // TODO: Remove unneeded methods
  def bombCells = cells.filter(_.hasBomb)

  def emptyCells = cells.toSet -- bombCells

  def revealedCells = cells.toSet.filter(_.isRevealed)

  def revealedBombCells = revealedCells.filter(_.hasBomb)

  def revealedEmptyCells = revealedCells -- revealedBombCells

  def remainingEmptyCells = emptyCells -- revealedEmptyCells


  def getCell(coordinates: Coordinates): Cell = {
    cellsByCoordinates(coordinates)
  }

  def setCellValue(coordinates: Coordinates, value: String): Board = {
    cellsByCoordinates(coordinates) = cellsByCoordinates(coordinates).copy(value = value)
    Board(totalRows, totalColumns, totalBombs, cellsByCoordinates)
  }

  def revealCell(cell: Cell): Board = {
    revealCell(cell.row, cell.column)
  }

  def revealCell(coordinates: Coordinates): Board = {
    cellsByCoordinates(coordinates) = cellsByCoordinates(coordinates).copy(isRevealed = true)
    Board(totalRows, totalColumns, totalBombs, cellsByCoordinates)
  }

  def adjacentCellsOf(cell: Cell): Seq[Cell] = {
    cellLocationContext.neighboursOf(cell).map(getCell)
  }
}
