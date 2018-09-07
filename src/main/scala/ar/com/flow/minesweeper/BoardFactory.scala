package ar.com.flow.minesweeper

import scala.util.Random

object BoardFactory {
  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Board = {
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

    apply(totalRows, totalColumns, totalBombs, cells)
  }

  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int, cells: Seq[Cell]): Board = {
    new Board(totalRows, totalColumns, totalBombs, cells)
  }
}
