package ar.com.flow.minesweeper

import scala.collection.mutable
import scala.collection.mutable.HashMap

class Board(val totalRows: Int, val totalColumns: Int, val totalBombs: Int, initialCells: Seq[Cell]) {
  private val cellMap: mutable.Map[(Int, Int), Cell] = HashMap(initialCells.map(c => (c.row, c.column) -> c): _*)
  private val cellLocationContext = new CellLocationContext(totalRows, totalColumns)
  private def cellsSet = cellMap.values.toSet

  def cells: Seq[Cell] = cellMap.values.toSeq

  def bombCells = cellsSet.filter(_.hasBomb)

  def emptyCells = cellsSet -- bombCells

  def revealedCells = cellsSet.filter(_.isRevealed)

  def revealedBombCells = revealedCells.filter(_.hasBomb)

  def revealedEmptyCells = revealedCells -- revealedBombCells

  def remainingEmptyCells = emptyCells -- revealedEmptyCells

  def getCell(row: Int, column: Int): Cell = {
    cellMap((row, column))
  }

  def setCellValue(row: Int, column: Int, value: String): Board = {
    cellMap((row, column)) = cellMap((row, column)).copy(value = value)
    new Board(totalRows, totalColumns, totalBombs, cells)
  }

  def revealCell(row: Int, column: Int) = {
    cellMap((row, column)) = cellMap((row, column)).copy(isRevealed = true)
    new Board(totalRows, totalColumns, totalBombs, cells)
  }

  def adjacentCellsOf(row: Int, column: Int): Seq[Cell] = {
    cellLocationContext.neighboursOf(row, column).map(cellMap)
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Board]

  override def equals(other: Any): Boolean = other match {
    case that: Board => (that canEqual this) && hashCode == that.hashCode
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(totalRows, totalColumns, totalBombs, cells)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
