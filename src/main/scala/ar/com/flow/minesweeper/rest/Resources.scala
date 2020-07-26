package ar.com.flow.minesweeper.rest

import java.time.format.DateTimeFormatter

import ar.com.flow.minesweeper.CellMark.{Flag, Question}
import ar.com.flow.minesweeper.GameRunningState.{Finished, Running}
import ar.com.flow.minesweeper.GamePauseResume.{Paused, Resumed}
import ar.com.flow.minesweeper.GameResult.{Lost, Won}
import ar.com.flow.minesweeper._

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  implicit val gamePauseResumeToString: GamePauseResume => String = {
    case Paused => "paused"
    case Resumed => "resumed"
  }
  implicit val gamePlayStatusToString: GameRunningState => String = {
    case Running => "playing"
    case Finished => "finished"
  }

  implicit val gameResult: Option[GameResult] => String = {
    case None => "pending"
    case Some(Won) => "won"
    case Some(Lost) => "lost"
  }

  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt.format(DateTimeFormatter.ISO_DATE_TIME), BoardResource(game.board), game.runningState, game.result)
  }
}

case class GameResource(id: String, createdAt: String, board: BoardResource, state: String, result: String)

object BoardResource {
  def apply(board: Board): BoardResource = {
    BoardResource(board.dimensions, CellResources.from(board))
  }
}

case class BoardResource(dimensions: Dimensions, cells: Seq[CellResource])

object CellResources {
  def from(board: Board): Seq[CellResource] = {
    board.cells.all.toSeq.map(CellResource.from).sorted
  }
}

object CellResource {
  implicit val cellContentToBoolean: CellContent => Boolean = {
    case CellContent.Bomb => true
    case CellContent.Empty => false
  }

  implicit val cellMarkToString: CellMark => String = {
    case Flag => "f"
    case Question => "?"
  }

  def from(cell: Cell): CellResource = {
    new CellResource(cell.coordinates, cell.content, cell.visibility, cell.mark.map(cellMarkToString))
  }
}

case class CellResource(coordinates: CartesianCoordinates, hasBomb: Boolean, visibility: Visibility, mark: Option[String] = None) extends Ordered[CellResource] {
  // https://stackoverflow.com/a/19348339/545273

  override def compare(that: CellResource): Int = {
    this.coordinates compare that.coordinates
  }
}
