package ar.com.flow.minesweeper

import java.util.{Date, UUID}

object GameFactory {
  def createGame(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, new Date(), BoardFactory(totalRows, totalColumns, totalBombs))
  }
}

class Game(val id: String, val createdAt: java.util.Date, var board: Board, var state: String = "playing", var result: String = "") {
  def flagCell(x: Int, y: Int) = {
    board = board.setCellValue(x, y, CellValue.flag)
  }

  def questionCell(x: Int, y: Int) = {
    board.setCellValue(x, y, CellValue.question)
  }

  def revealCell(x: Int, y: Int) = {
    board = board.revealCell(x, y)

    result = if (board.getCell(x, y).hasBomb) "lost" else if (board.revealedEmptyCells == board.emptyCells) "won" else ""

    state = result match {
      case "won" | "lost" => "finished"
      case _ => "playing"
    }
  }
}
