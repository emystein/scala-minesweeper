package ar.com.flow.minesweeper

import scala.collection.mutable.HashMap

case class Board(totalRows: Int, totalColumns: Int, totalBombs: Int, initialCells: Seq[Cell]) {
  private val cellMap = HashMap(initialCells.map(c => (c.row, c.column) -> c): _*)
  private def cellsSet = cellMap.values.toSet

  def cells: Seq[Cell] = cellMap.values.toSeq.sorted

  def bombCells = cellsSet.filter(_.hasBomb)

  def emptyCells = cellsSet -- bombCells

  def revealedCells = cellsSet.filter(_.isRevealed)

  def revealedBombCells = revealedCells.filter(_.hasBomb)

  def revealedEmptyCells = revealedCells -- revealedBombCells

  def remainingEmptyCells = emptyCells -- revealedEmptyCells

  def getCell(coordinates: (Int, Int)): Cell = {
    cellMap(coordinates)
  }

  def setCellValue(coordinates: (Int, Int), value: String): Board = {
    cellMap(coordinates) = cellMap(coordinates).copy(value = value)
    Board(totalRows, totalColumns, totalBombs, cells)
  }

  def revealCell(cell: Cell): Board = {
    revealCell(cell.row, cell.column)
  }

  def revealCell(row: Int, column: Int): Board = {
    cellMap((row, column)) = cellMap((row, column)).copy(isRevealed = true)
    Board(totalRows, totalColumns, totalBombs, cells)
  }
}
