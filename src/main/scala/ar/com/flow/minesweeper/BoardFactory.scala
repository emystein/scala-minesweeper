package ar.com.flow.minesweeper

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object BoardFactory {
  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Board = {
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
        new Cell(row, column, hasBomb, adjacentBombs, false)
      }

      cells.map(_.to[ArrayBuffer]).to[ArrayBuffer]
    }

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
