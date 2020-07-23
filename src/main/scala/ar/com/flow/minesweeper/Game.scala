package ar.com.flow.minesweeper

import java.time.LocalDateTime
import java.util.UUID

object Game {
  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, LocalDateTime.now, Board(Dimensions(totalRows, totalColumns), totalBombs))
  }
}

// TODO: Make board, state a val
class Game(val id: String, val createdAt: LocalDateTime, var board: Board, var state: GameState = GameState(GamePlayStatus.playing, GameResult.pending)) {
  def flagCell(coordinates: CartesianCoordinates): Unit = {
    board = board.markCell(coordinates, Some(CellMark.Flag))
  }

  def questionCell(coordinates: CartesianCoordinates): Unit = {
    board = board.markCell(coordinates, Some(CellMark.Question))
  }

  def revealCell(coordinates: CartesianCoordinates): Unit = {
    val cell = board.cellAt(coordinates)

    board = board.revealCell(coordinates)

    // TODO: Use EmptyCell / BombCell polymorphism to remove this if
    if (cell.content.isEmpty) {
      cell.adjacentEmptySpace().foreach(cell => board = board.revealCell(cell.coordinates))
    }

    state = GameState(board)
  }

  def pause(): Unit = {
    state = switchPlayStatusTo(GamePlayStatus.paused)
  }

  def resume(): Unit = {
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
