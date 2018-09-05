package ar.com.flow.minesweeper

import scala.collection.mutable


class Board(val totalRows: Int, val totalColumns: Int, val totalBombs: Int, var cells: mutable.Map[(Int, Int), Cell]) {
  private val cellLocationContext = new CellLocationContext(totalRows, totalColumns)

  private def cellsSet = cells.values.toSet

  def bombCells = cellsSet.filter(_.hasBomb)

  def emptyCells = cellsSet -- bombCells

  def revealedCells = cellsSet.filter(_.isRevealed)

  def revealedBombCells = revealedCells.filter(_.hasBomb)

  def revealedEmptyCells = revealedCells -- revealedBombCells

  def remainingEmptyCells = emptyCells -- revealedEmptyCells

  def getCell(row: Int, column: Int): Cell = {
    cells((row, column))
  }

  def setCellValue(row: Int, column: Int, value: String): Board = {
    cells((row, column)) = cells((row, column)).copy(value = value)
    new Board(totalRows, totalColumns, totalBombs, cells)
  }

  def revealCell(row: Int, column: Int) = {
    cells((row, column)) = cells((row, column)).copy(isRevealed = true)
    new Board(totalRows, totalColumns, totalBombs, cells)
  }

  def adjacentCellsOf(row: Int, column: Int): Seq[Cell] = {
    cellLocationContext.neighboursOf(row, column).map(cells)
  }
}
