package ar.com.flow.minesweeper

import scala.collection.mutable.ArrayBuffer


class Board(val totalRows: Int, val totalColumns: Int, val totalBombs: Int, var cells: ArrayBuffer[ArrayBuffer[Cell]]) {
  private def cellsSet: Set[Cell] = cells.flatten.toSet

  def bombCells: Set[Cell] = cellsSet.filter(_.hasBomb)

  def emptyCells: Set[Cell] = cellsSet -- bombCells

  def revealedCells: Set[Cell] = cellsSet.filter(_.isRevealed)

  def revealedEmptyCells: Set[Cell] = revealedCells.filter(!_.hasBomb)

  def setCellValue(row: Int, column: Int, value: String): Board = {
    cells(row - 1)(column - 1) = cells(row - 1)(column - 1).copy(value = value)
    new Board(totalRows, totalColumns, totalBombs, cells)
  }

  def getCell(row: Int, column: Int): Cell = {
    cells(row - 1)(column - 1)
  }

  def revealCell(row: Int, column: Int) = {
    cells(row - 1)(column - 1) = cells(row - 1)(column - 1).copy(isRevealed = true)
    new Board(totalRows, totalColumns, totalBombs, cells)
  }

  def neighboursOf(row: Int, column: Int): Seq[(Int, Int)] = {
    for {
      x <- row - 1 to row + 1
      y <- column - 1 to column + 1
      if x != row || y != column
    } yield (x, y)
  }
}
