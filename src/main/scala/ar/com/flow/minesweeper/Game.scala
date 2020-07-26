package ar.com.flow.minesweeper

import java.time.LocalDateTime
import java.util.UUID

import ar.com.flow.minesweeper.CellContent.Bomb
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
    val revealedCellBoard = board.revealCellAt(coordinates)

    revealedCellBoard.cellAt(coordinates).content match {
      case Bomb => lostGame(revealedCellBoard)
      case _    => continuePlayingGame(revealedCellBoard, coordinates)
    }
  }

  private def lostGame(revealedCellBoard: Board): Game = {
    copy(board = revealedCellBoard, playStatus = GamePlayState.Finished, result = Some(GameResult.Lost))
  }

  private def continuePlayingGame(revealedCellBoard: Board, coordinates: CartesianCoordinates): Game = {
    val revealedCell = revealedCellBoard.cellAt(coordinates)

    val updatedBoard = revealedCell.adjacentEmptySpace().foldLeft(revealedCellBoard)((board, cell) => board.revealCellAt(cell.coordinates))

    if (updatedBoard.cells.hidden.empty.isEmpty) {
      // if recursive cell reveal won the game
      copy(board = updatedBoard, playStatus = GamePlayState.Finished, result = Some(GameResult.Won))
    } else {
      copy(board = updatedBoard)
    }
  }

  def togglePauseResume: Game = copy(playStatus = playStatus.next())
}
