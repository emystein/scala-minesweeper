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

case class Game(id: String,
                createdAt: LocalDateTime = LocalDateTime.now,
                board: Board,
                playStatus: GamePlayState = GamePlayState.Running,
                result: Option[GameResult] = None) {

  def toggleCellMark(coordinates: CartesianCoordinates): Game = {
      copy(board = board.toggleMarkAt(coordinates))
  }

  def revealCell(coordinates: CartesianCoordinates): Game = {
    val cell = board.cellAt(coordinates)

    val revealedCellBoard = board.revealCellAt(coordinates)

    if (cell.content == CellContent.Bomb) {
      return copy(board = revealedCellBoard, playStatus = GamePlayState.Finished, result = Some(GameResult.Lost))
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
      updatedPlayStatus = GamePlayState.Finished
      updatedResult = Some(GameResult.Won)
    }

    copy(board = updatedBoard, playStatus = updatedPlayStatus, result = updatedResult)
  }

  def togglePauseResume: Game = copy(playStatus = playStatus.next())
}
