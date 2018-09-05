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

  def revealAdjacentCellsRecursive(row: Int, column: Int, alreadyTraversedCells: Set[(Int, Int)]): Set[(Int, Int)] = {
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

  def canEqual(other: Any): Boolean = other.isInstanceOf[Game]

  override def equals(other: Any): Boolean = other match {
    case that: Game => (that canEqual this) && hashCode == that.hashCode
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(createdAt, board)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
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