package ar.com.flow.minesweeper

import java.time.LocalDateTime
import java.util.UUID

import ar.com.flow.minesweeper.CellContent.Bomb

object Game {
  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    RunningGame(id = UUID.randomUUID().toString, createdAt = LocalDateTime.now, Board(Dimensions(totalRows, totalColumns), totalBombs))
  }
}

abstract class Game(val id: String,
                    val createdAt: LocalDateTime,
                    val board: Board,
                    val runningState: GameRunningState) {

  def toggleCellMark(coordinates: CartesianCoordinates): Game

  def revealCell(coordinates: CartesianCoordinates): Game

  def togglePauseResume: Game

  def result: Option[GameResult]
}

case class RunningGame(override val id: String,
                       override val createdAt: LocalDateTime,
                       override val board: Board) extends Game(id, createdAt, board, GameRunningState.Running) {

  def toggleCellMark(coordinates: CartesianCoordinates): Game = copy(board = board.toggleMarkAt(coordinates))

  def revealCell(coordinates: CartesianCoordinates): Game = {
    val updatedBoard = board.cellAt(coordinates).content match {
      case Bomb => board.revealCellAt(coordinates)
      case _ => board.revealCellAndAdjacentAt(coordinates)
    }

    if (updatedBoard.hasAllEmptyCellsRevealed || updatedBoard.hasACellWithBombRevealed) {
      FinishedGame(id, createdAt, updatedBoard)
    } else {
      copy(board = updatedBoard)
    }
  }

  def togglePauseResume: Game = PausedGame(id, createdAt, board)

  def result: Option[GameResult] = None
}

abstract class FrozenCellsGame(id: String, createdAt: LocalDateTime, board: Board, pauseResume: GameRunningState)
  extends Game(id, createdAt, board, pauseResume) {

  def toggleCellMark(coordinates: CartesianCoordinates): Game = this
  def revealCell(coordinates: CartesianCoordinates): Game = this
}

case class PausedGame(override val id: String, override val createdAt: LocalDateTime, override val board: Board)
  extends FrozenCellsGame(id, createdAt, board, GameRunningState.Paused) {

  def togglePauseResume: Game = RunningGame(id, createdAt, board)

  def result: Option[GameResult] = None
}

case class FinishedGame(override val id: String, override val createdAt: LocalDateTime, override val board: Board)
  extends FrozenCellsGame(id, createdAt, board, GameRunningState.Finished) {

  def togglePauseResume: Game = this

  def result: Option[GameResult] = {
    if (board.hasAllEmptyCellsRevealed) {
      Some(GameResult.Won)
    } else {
      Some(GameResult.Lost)
    }
  }
}
