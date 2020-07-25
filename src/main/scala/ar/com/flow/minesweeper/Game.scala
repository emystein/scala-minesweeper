package ar.com.flow.minesweeper

import java.time.LocalDateTime
import java.util.UUID

import ar.com.flow.minesweeper.CellMark.{Flag, Question}
import ar.com.flow.minesweeper.GamePlayStatus.{paused, playing}
import ar.com.flow.minesweeper.Visibility.Hidden

object Game {
  def apply(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, LocalDateTime.now, Board(Dimensions(totalRows, totalColumns), totalBombs))
  }
}

// TODO: Make board, state a val
case class Game(id: String,
                createdAt: LocalDateTime = LocalDateTime.now,
                board: Board,
                state: GameState = GameState(playing, GameResult.pending)) {

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
    if (board.cellAt(coordinates).visibility == Hidden) {
      new Game(id, createdAt, board = board.markCellAt(coordinates, cellMark), state)
    } else {
      this
    }
  }

  def revealCell(coordinates: CartesianCoordinates): Game = {
    val cell = board.cellAt(coordinates)

    val revealedCellBoard = board.revealCellAt(coordinates)

    if (cell.content == CellContent.Bomb) {
      return copy(board = revealedCellBoard, state = GameState(GamePlayStatus.finished, GameResult.lost))
    }

    var updatedBoard = revealedCellBoard

    // TODO: Use EmptyCell / BombCell polymorphism to remove this if
    if (cell.content == CellContent.Empty) {
      updatedBoard = revealedCellBoard.adjacentEmptySpace(cell).foldLeft(revealedCellBoard)((board, cell) => board.revealCellAt(cell.coordinates))
    }

    var updatedGameState = state

    if (updatedBoard.cells.hidden.empty.isEmpty) {
      // if recursive cell reveal won the game
      updatedGameState = GameState(GamePlayStatus.finished, GameResult.won)
    }

    copy(board = updatedBoard, state = updatedGameState)
  }

  def pause(): Game = {
    switchPlayStatusTo(paused)
  }

  def resume(): Game = {
    switchPlayStatusTo(playing)
  }

  // TODO: implement State pattern ?
  private def switchPlayStatusTo(status: String): Game = {
    val newState = state match {
      case GameState(GamePlayStatus.finished, _) => state
      case _ => GameState(status, state.result)
    }

    copy(state = newState)
  }

//  def canEqual(other: Any): Boolean = other.isInstanceOf[Game]
//
//  override def equals(other: Any): Boolean = other match {
//    case that: Game => (that canEqual this) && hashCode == that.hashCode
//    case _ => false
//  }
//
//  override def hashCode(): Int = {
//    val state = Seq(createdAt, board)
//    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
//  }
}
