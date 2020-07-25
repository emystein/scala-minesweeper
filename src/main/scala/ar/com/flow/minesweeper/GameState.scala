package ar.com.flow.minesweeper

abstract sealed class GamePlayStatus extends Product with Serializable

object GamePlayStatus {
  final case object Playing extends GamePlayStatus
  final case object Paused extends GamePlayStatus
  final case object Finished extends GamePlayStatus
}

abstract sealed class GameResult extends Product with Serializable

object GameResult {
  final case object Pending extends GameResult
  final case object Won extends GameResult
  final case object Lost extends GameResult

  def of(revealedBombCells: Iterable[Cell], remainingEmptyCells: Iterable[Cell]): GameResult = {
    if (revealedBombCells.nonEmpty)
      Lost
    else if (remainingEmptyCells.isEmpty)
      Won
    else
      Pending
  }
}

object GameState {
  def apply(board: Board): GameState = {
    val result = GameResult.of(board.cells.revealed.withBomb(), board.cells.hidden.empty)

    val playStatus: GamePlayStatus = result match {
      case GameResult.Pending => GamePlayStatus.Playing
      case _ => GamePlayStatus.Finished
    }

    GameState(playStatus, result)
  }
}

case class GameState(playStatus: GamePlayStatus, result: GameResult)
