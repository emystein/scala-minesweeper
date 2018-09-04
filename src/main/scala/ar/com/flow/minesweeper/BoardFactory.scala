package ar.com.flow.minesweeper

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scala.collection.mutable.Map

object BoardFactory {
  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Board = {
    val cells: Map[(Int, Int), Cell] = {
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
        column <- 1 to totalColumns
      } yield {
        val hasBomb = bombCoordinates.contains(row, column)
        val adjacentBombs = neighboursOf(row, column, totalRows, totalColumns).count(bombCoordinates.contains)
        new Cell(row, column, hasBomb, adjacentBombs)
      }

      val builder = mutable.HashMap.newBuilder ++= cells.map(c => (c.row, c.column) -> c)
      builder.result()
    }

    new Board(totalRows, totalColumns, totalBombs, cells)
  }

  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int, cellSeq: Seq[Cell]): Board = {
    val builder = mutable.HashMap.newBuilder ++= cellSeq.map(c => (c.row, c.column) -> c)
    val cells = builder.result()
    new Board(totalRows, totalColumns, totalBombs, cells)
  }

  def neighboursOf(row: Int, column: Int, totalRows: Int, totalColumns: Int): Seq[(Int, Int)] = {
    for {
      x <- row - 1 to row + 1
      y <- column - 1 to column + 1
      if (x > 0 && x <= totalRows) && (y > 0 && y <= totalColumns) && (x != row || y != column)
    } yield (x, y)
  }
}
