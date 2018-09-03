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

  def revealCell(row: Int, column: Int): Unit = {
    board = board.revealCell(row, column)

    revealAdjacentCellsRecursive(row, column, Set((row, column)))
  }

  def revealAdjacentCellsRecursive(row: Int, column: Int, alreadyTraversedCells: Set[(Int, Int)]): Unit = {
    if (board.getCell(row, column).hasBomb) {
      Set()
    } else {
      board = board.revealCell(row, column)

      val adjacentCells = board.adjacentCellsOf(row, column)
        .map(cell => (cell.row, cell.column))
        .toSet -- alreadyTraversedCells

      val newTraversed = alreadyTraversedCells ++ adjacentCells ++ Set((row, column))

      adjacentCells.foreach(coordinates =>
        revealAdjacentCellsRecursive(coordinates._1, coordinates._2, newTraversed)
      )

      newTraversed
    }
  }

  def result = GameResult.of(board)

  def state = result match {
    case GameResult.pending => GameState.playing
    case _ => GameState.finished
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

  def of(board: Board): String = {
    if (!board.revealedBombCells.isEmpty)
      lost
    else if (board.remainingEmptyCells.isEmpty)
      won
    else
      pending
  }
}