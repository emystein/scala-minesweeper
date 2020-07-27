package ar.com.flow.minesweeper

abstract sealed class GamePauseResume extends Product with Serializable

object GamePauseResume {
  final case object Paused extends GamePauseResume
  final case object Resumed extends GamePauseResume
}

abstract sealed class GameRunningState extends Product with Serializable

object GameRunningState {
  final case object Running extends GameRunningState
  final case object Paused extends GameRunningState
  final case object Finished extends GameRunningState
}

abstract sealed class GameResult extends Product with Serializable

object GameResult {
  final case object Won extends GameResult
  final case object Lost extends GameResult

  def of(revealedBombCells: Iterable[Cell], remainingEmptyCells: Iterable[Cell]): Option[GameResult] = {
    if (revealedBombCells.nonEmpty)
      Some(Lost)
    else if (remainingEmptyCells.isEmpty)
      Some(Won)
    else
      None
  }
}
