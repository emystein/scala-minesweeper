package ar.com.flow.minesweeper

import scala.collection.mutable.HashMap

class Board(val totalRows: Int, val totalColumns: Int, val totalBombs: Int, initialCells: Seq[Cell]) {
  private val cellMap = HashMap(initialCells.map(c => (c.row, c.column) -> c): _*)
  private def cellsSet = cellMap.values.toSet

  def cells: Seq[Cell] = cellMap.values.toSeq.sorted

  def bombCells = cellsSet.filter(_.hasBomb)

  def emptyCells = cellsSet -- bombCells

  def revealedCells = cellsSet.filter(_.isRevealed)

  def revealedBombCells = revealedCells.filter(_.hasBomb)

  def revealedEmptyCells = revealedCells -- revealedBombCells

  def remainingEmptyCells = emptyCells -- revealedEmptyCells

  def getCell(row: Int, column: Int): Cell = {
    cellMap((row, column))
  }

  def getCell(coordinates: (Int, Int)): Cell = {
    cellMap(coordinates._1, coordinates._2)
  }

  def setCellValue(coordinates: (Int, Int), value: String): Board = {
    cellMap(coordinates) = cellMap(coordinates).copy(value = value)
    new Board(totalRows, totalColumns, totalBombs, cells)
  }
  def setCellValue(row: Int, column: Int, value: String): Board = {
    setCellValue((row, column), value)
  }

  def revealCell(cell: Cell): Board = {
    revealCell(cell.row, cell.column)
  }

  def revealCell(row: Int, column: Int): Board = {
    cellMap((row, column)) = cellMap((row, column)).copy(isRevealed = true)
    new Board(totalRows, totalColumns, totalBombs, cells)
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
