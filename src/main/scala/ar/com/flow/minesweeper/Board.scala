package ar.com.flow.minesweeper

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Board(val totalRows: Int, val totalColumns: Int, val totalBombs: Int) {
  // TODO: make cells immutable
  val cells: ArrayBuffer[ArrayBuffer[Cell]] = {
    val allCoordinates = for {
      rowNumber <- 1 to totalRows
      columnNumber <- 1 to totalColumns
    } yield {
      (rowNumber, columnNumber)
    }

    val randomCoordinates = Random.shuffle(allCoordinates)
    val bombCoordinates = randomCoordinates.take(totalBombs)

    val cells = for {
      row <- 1 to totalRows
    } yield for {
      column <- 1 to totalColumns
    } yield {
      val hasBomb = bombCoordinates.contains(row, column)
      val adjacentBombs = neighboursOf(row, column).count(bombCoordinates.contains)
      new Cell(row, column, hasBomb, adjacentBombs, false, CellValue.NONE)
    }

    cells.map(_.to[ArrayBuffer]).to[ArrayBuffer]
  }

  private def cellsSet: Set[Cell] = cells.flatten.toSet
  def bombCells: Set[Cell] = cellsSet.filter(_.hasBomb)
  def emptyCells: Set[Cell] = cellsSet -- bombCells
  def revealedCells: Set[Cell] = cellsSet.filter(_.isRevealed)
  def revealedEmptyCells: Set[Cell] = revealedCells.filter(!_.hasBomb)

  def setCellValue(row: Int, column: Int, value: CellValue.Value) = {
    cells(row - 1)(column - 1) = cells(row - 1)(column - 1).copy(value = value)
  }

  def getCell(row: Int, column: Int): Cell = {
    cells(row - 1)(column - 1)
  }

  def revealCell(row: Int, column: Int) = {
    cells(row - 1)(column - 1) = cells(row - 1)(column - 1).copy(isRevealed = true)
  }

  def neighboursOf(row: Int, column: Int): Seq[(Int, Int)] = {
    for {
      x <- row - 1 to row + 1
      y <- column - 1 to column + 1
      if x != row || y != column
    } yield (x, y)
  }
}
