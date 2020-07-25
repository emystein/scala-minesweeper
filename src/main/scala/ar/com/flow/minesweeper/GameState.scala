package ar.com.flow.minesweeper

abstract sealed class GamePlayState extends Product with Serializable {
  def next(): GamePlayState
}

object GamePlayState {
  final case object Running extends GamePlayState {
    override def next(): GamePlayState = GamePlayState.Paused
  }
  final case object Paused extends GamePlayState {
    override def next(): GamePlayState = GamePlayState.Running
  }
  final case object Finished extends GamePlayState {
    override def next(): GamePlayState = GamePlayState.Finished
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
