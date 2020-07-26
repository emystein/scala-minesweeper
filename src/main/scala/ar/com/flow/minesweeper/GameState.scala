package ar.com.flow.minesweeper

abstract sealed class GamePauseResume extends Product with Serializable {
  def toggle(): GamePauseResume
}

object GamePauseResume {
  final case object Paused extends GamePauseResume {
    override def toggle(): GamePauseResume = GamePauseResume.Resumed
  }
  final case object Resumed extends GamePauseResume {
    override def toggle(): GamePauseResume = GamePauseResume.Paused
  }
}

abstract sealed class GameRunningState extends Product with Serializable {
  def next(): GameRunningState
}

object GameRunningState {
  final case object Running extends GameRunningState {
    override def next(): GameRunningState = GameRunningState.Finished
  }
  final case object Finished extends GameRunningState {
    override def next(): GameRunningState = GameRunningState.Finished
  }
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
