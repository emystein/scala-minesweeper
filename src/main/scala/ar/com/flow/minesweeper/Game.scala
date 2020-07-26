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
                    val pauseResume: GamePauseResume = GamePauseResume.Resumed) {

  def toggleCellMark(coordinates: CartesianCoordinates): Game

  def revealCell(coordinates: CartesianCoordinates): Game

  def togglePauseResume: Game

  def runningState: GameRunningState = {
    if (board.cells.hidden.empty.isEmpty || board.cells.revealed.withBomb.nonEmpty) {
      GameRunningState.Finished
    } else {
      GameRunningState.Running
    }
  }

  def result: Option[GameResult] = {
    if (board.cells.hidden.empty.isEmpty) {
      Some(GameResult.Won)
    } else if (board.cells.revealed.withBomb.nonEmpty) {
      Some(GameResult.Lost)
    } else {
      None
    }
  }
}

case class RunningGame(override val id: String,
                       override val createdAt: LocalDateTime,
                       override val board: Board) extends Game(id, createdAt, board, GamePauseResume.Resumed) {

  def toggleCellMark(coordinates: CartesianCoordinates): Game = copy(board = board.toggleMarkAt(coordinates))

  def revealCell(coordinates: CartesianCoordinates): Game = {
    val updatedBoard = board.cellAt(coordinates).content match {
      case Bomb => board.revealCellAt(coordinates)
      case _ => board.revealCellAndAdjacentAt(coordinates)
    }

    copy(board = updatedBoard)
  }

  def togglePauseResume: Game = PausedGame(id, createdAt, board)
}

case class PausedGame(override val id: String,
                      override val createdAt: LocalDateTime,
                      override val board: Board) extends Game(id, createdAt, board, GamePauseResume.Paused) {

  def toggleCellMark(coordinates: CartesianCoordinates): Game = this

  def revealCell(coordinates: CartesianCoordinates): Game = this

  def togglePauseResume: Game = RunningGame(id, createdAt, board)
}
