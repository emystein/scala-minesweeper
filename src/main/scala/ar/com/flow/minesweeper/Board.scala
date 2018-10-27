package ar.com.flow.minesweeper

import scala.collection.mutable.HashMap

case class Board(totalRows: Int, totalColumns: Int, totalBombs: Int, initialCells: Seq[Cell]) {
  private val cellMap = HashMap(initialCells.map(c => (c.row, c.column) -> c): _*)
  private def cellsSet = cellMap.values.toSet

  val cellLocationContext = new CellLocationContext(totalRows, totalColumns)

  def cells: Seq[Cell] = cellMap.values.toSeq.sorted

  // TODO: Remove unneeded methods
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

  def revealCell(coordinates: (Int, Int)): Board = {
    cellMap(coordinates) = cellMap(coordinates).copy(isRevealed = true)
    Board(totalRows, totalColumns, totalBombs, cells)
  }
}
