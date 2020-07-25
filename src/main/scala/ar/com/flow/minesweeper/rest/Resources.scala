package ar.com.flow.minesweeper.rest

import java.time.format.DateTimeFormatter

import ar.com.flow.minesweeper.CellMark.{Flag, Question}
import ar.com.flow.minesweeper.GamePlayStatus.{Finished, Paused, Playing}
import ar.com.flow.minesweeper.GameResult.{Lost, Pending, Won}
import ar.com.flow.minesweeper.{Board, CartesianCoordinates, Cell, CellContent, CellMark, Cells, Dimensions, Game, GamePlayStatus, GameResult, Visibility}

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  implicit val gamePlayStatusToString: GamePlayStatus => String = {
    case Playing => "playing"
    case Paused => "paused"
    case Finished => "finished"
  }

  implicit val gameResult: GameResult => String = {
    case Pending => "pending"
    case Won => "won"
    case Lost => "lost"
  }

  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt.format(DateTimeFormatter.ISO_DATE_TIME), BoardResource(game.board), game.state.playStatus, game.state.result)
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
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: CellResource): Int = {
    this.coordinates compare that.coordinates
  }
}
