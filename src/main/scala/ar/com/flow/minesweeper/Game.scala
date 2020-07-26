package ar.com.flow.minesweeper

import java.time.LocalDateTime
import java.util.UUID

import ar.com.flow.minesweeper.CellContent.Bomb

object Game {
  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, LocalDateTime.now, Board(Dimensions(totalRows, totalColumns), totalBombs))
  }
}

case class Game(id: String,
                createdAt: LocalDateTime = LocalDateTime.now,
                board: Board,
                pauseResume: GamePauseResume = GamePauseResume.Resumed) {

  def toggleCellMark(coordinates: CartesianCoordinates): Game = {
      copy(board = board.toggleMarkAt(coordinates))
  }

  def revealCell(coordinates: CartesianCoordinates): Game = {
    val updatedBoard = board.cellAt(coordinates).content match {
      case Bomb => board.revealCellAt(coordinates)
      case _    => board.revealCellAndAdjacentAt(coordinates)
    }

    copy(board = updatedBoard)
  }

  def togglePauseResume: Game = copy(pauseResume = pauseResume.toggle())

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
