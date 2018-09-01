package ar.com.flow.minesweeper

import java.util.{Date, UUID}

object GameFactory {
  def createGame(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, new Date(), new Board(totalRows, totalColumns, totalBombs))
  }
}

class Game(val id: String, val createdAt: java.util.Date, val board: Board, var state: String = "playing", var result: String = "") {
  // TODO: maybe just expose setCellValue instead of having questionCell, flagCell, etc.
  def questionCell(x: Int, y: Int) = {
    board.setCellValue(x, y, CellValue.QUESTION)
  }

  def flagCell(x: Int, y: Int) = {
    board.setCellValue(x, y, CellValue.FLAG)
  }

  def revealCell(x: Int, y: Int) = {
    board.revealCell(x, y)

    result = if (board.getCell(x, y).hasBomb) "lost" else if (board.revealedEmptyCells == board.emptyCells) "won" else ""

    state = result match {
      case "won" | "lost" => "finished"
      case _ => "playing"
    }
  }
}
