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
case class Game(id: String,
                createdAt: LocalDateTime = LocalDateTime.now,
                board: Board,
                playStatus: GamePlayStatus = GamePlayStatus.Playing,
                result: GameResult = GameResult.Pending) {

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
      copy(board = board.markCellAt(coordinates, cellMark))
    } else {
      this
    }
  }

  def revealCell(coordinates: CartesianCoordinates): Game = {
    val cell = board.cellAt(coordinates)

    val revealedCellBoard = board.revealCellAt(coordinates)

    if (cell.content == CellContent.Bomb) {
      return copy(board = revealedCellBoard, playStatus = GamePlayStatus.Finished, result = GameResult.Lost)
    }

    var updatedBoard = revealedCellBoard

    // TODO: Use EmptyCell / BombCell polymorphism to remove this if
    if (cell.content == CellContent.Empty) {
      updatedBoard = revealedCellBoard.adjacentEmptySpace(cell).foldLeft(revealedCellBoard)((board, cell) => board.revealCellAt(cell.coordinates))
    }

    var updatedPlayStatus = playStatus
    var updatedResult = result

    if (updatedBoard.cells.hidden.empty.isEmpty) {
      // if recursive cell reveal won the game
      updatedPlayStatus = GamePlayStatus.Finished
      updatedResult = GameResult.Won
    }

    copy(board = updatedBoard, playStatus = updatedPlayStatus, result = updatedResult)
  }

  def pause(): Game = {
    switchPlayStatusTo(GamePlayStatus.Paused)
  }

  def resume(): Game = {
    switchPlayStatusTo(GamePlayStatus.Playing)
  }

  private def switchPlayStatusTo(newPlayStatus: GamePlayStatus): Game = {
    val updatedStatus = playStatus match {
      case GamePlayStatus.Finished => GamePlayStatus.Finished
      case _ => newPlayStatus
    }

    copy(playStatus = updatedStatus)
  }
}
