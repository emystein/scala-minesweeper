package ar.com.flow.minesweeper

abstract sealed class GamePlayStatus extends Product with Serializable {
  def next(): GamePlayStatus
}

object GamePlayStatus {
  final case object Playing extends GamePlayStatus {
    override def next(): GamePlayStatus = GamePlayStatus.Paused
  }
  final case object Paused extends GamePlayStatus {
    override def next(): GamePlayStatus = GamePlayStatus.Playing
  }
  final case object Finished extends GamePlayStatus {
    override def next(): GamePlayStatus = GamePlayStatus.Finished
  }
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
