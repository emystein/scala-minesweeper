package ar.com.flow.minesweeper

import java.time.LocalDateTime
import java.util.UUID

import ar.com.flow.minesweeper.CellMark.{Flag, Question}
import ar.com.flow.minesweeper.Visibility.Hidden

object Game {
  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, LocalDateTime.now, Board(Dimensions(totalRows, totalColumns), totalBombs))
  }
}

// TODO: Make board, state a val
class Game(val id: String, val createdAt: LocalDateTime, var board: Board, var state: GameState = GameState(GamePlayStatus.playing, GameResult.pending)) {
  def advanceCellState(coordinates: CartesianCoordinates): Game = {
      val newCellMark: Option[CellMark] = board.cellAt(coordinates).mark match {
        case None => Some(Flag)
        case Some(Flag) => Some(Question)
        case Some(Question) => None
      }
      markCell(coordinates, newCellMark)
  }

  def flagCell(coordinates: CartesianCoordinates): Game = {
    markCell(coordinates, Some(Flag))
  }

  def questionCell(coordinates: CartesianCoordinates): Game = {
      markCell(coordinates, Some(Question))
  }

  private def markCell(coordinates: CartesianCoordinates, cellMark: Option[CellMark]): Game = {
    if (board.cellAt(coordinates).visibility == Hidden)
      board = board.markCellAt(coordinates, cellMark)

    this
  }

  def revealCell(coordinates: CartesianCoordinates): Game = {
    val cell = board.cellAt(coordinates)

    board = board.revealCellAt(coordinates)

    // TODO: Use EmptyCell / BombCell polymorphism to remove this if
    if (cell.content == CellContent.Empty) {
      board.adjacentEmptySpace(cell).foreach(cell => board = board.revealCellAt(cell.coordinates))
    }

    state = GameState(board)

    this
  }

  def pause(): Game = {
    state = switchPlayStatusTo(GamePlayStatus.paused)

    this
  }

  def resume(): Game = {
    state = switchPlayStatusTo(GamePlayStatus.playing)

    this
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
