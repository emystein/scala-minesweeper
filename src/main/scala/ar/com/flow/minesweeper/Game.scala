package ar.com.flow.minesweeper

import java.time.LocalDateTime
import java.util.UUID

object Game {
  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, LocalDateTime.now, BoardFactory(totalRows, totalColumns, totalBombs))
  }
}

// TODO: Make board, state a val
class Game(val id: String, val createdAt: LocalDateTime, var board: Board, var state: GameState = GameState(GamePlayStatus.playing, GameResult.pending)) {

  private val emptyCellFinder = new AdjacentEmptyCellFinder(board)

  def flagCell(coordinates: (Int, Int)): Unit = {
    setCellValue(coordinates, CellValue.flag)
  }

  def questionCell(coordinates: (Int, Int)): Unit = {
    setCellValue(coordinates, CellValue.question)
  }

  def setCellValue(coordinates: (Int, Int), value: String): Unit = {
    board = board.setCellValue(coordinates, value)
  }

  def revealCell(coordinates: (Int, Int)): Unit = {
    val cell = board.getCell(coordinates)

    board = board.revealCell(cell)

    // TODO: Use EmptyCell / BombCell polymorphism to remove this if
    if (!cell.hasBomb) {
      emptyCellFinder.traverseEmptyAdjacentCells(cell).foreach(cell => board = board.revealCell(cell))
    }

    state = GameState(board)
  }

  def pause = {
    state = switchPlayStatusTo(GamePlayStatus.paused)
  }

  def resume = {
    state = switchPlayStatusTo(GamePlayStatus.playing)
  }

  // TODO: implement State pattern ?
  private def switchPlayStatusTo(status: String): GameState = {
    state match {
      case GameState(GamePlayStatus.finished, _) => state
      case _ => GameState(status, state.result)
    }
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
