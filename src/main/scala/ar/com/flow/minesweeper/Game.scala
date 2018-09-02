package ar.com.flow.minesweeper

import java.util.{Date, UUID}

object GameFactory {
  def createGame(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, new Date(), BoardFactory(totalRows, totalColumns, totalBombs))
  }
}

// TODO: Make board a val
class Game(val id: String, val createdAt: java.util.Date, var board: Board) {
  def flagCell(row: Int, column: Int) = {
    board = board.setCellValue(row, column, CellValue.flag)
  }

  def questionCell(row: Int, column: Int) = {
    board = board.setCellValue(row, column, CellValue.question)
  }

  def revealCell(row: Int, column: Int) = {
    board = board.revealCell(row, column)
  }

  def result =
    if (!board.revealedBombCells.isEmpty)
      GameResult.lost
    else if (board.remainingEmptyCells.isEmpty)
      GameResult.won
    else
      GameResult.pending

  def state = result match {
    case GameResult.pending => GameState.playing
    case GameResult.won | GameResult.lost => GameState.finished
  }
}

object GameState {
  val playing = "playing"
  val finished = "finished"
}

object GameResult {
  val pending = "pending"
  val won = "won"
  val lost = "lost"
}