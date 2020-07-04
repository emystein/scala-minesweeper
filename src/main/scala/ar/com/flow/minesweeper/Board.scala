package ar.com.flow.minesweeper

import scala.collection.mutable.HashMap
import scala.util.Random

object Board {
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
        new Cell(row, column, hasBomb, adjacentBombs)
      }
    }

    Board(totalRows, totalColumns, totalBombs, cells)
  }
}

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

  def adjacentCellsOf(cell: Cell): Seq[Cell] = {
    cellLocationContext.neighboursOf(cell).map(getCell)
  }
}
