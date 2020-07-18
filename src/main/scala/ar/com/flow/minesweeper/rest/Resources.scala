package ar.com.flow.minesweeper.rest

import java.time.format.DateTimeFormatter

import ar.com.flow.minesweeper.{Board, Cell, Visibility, Cells, Dimensions, Game}

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt.format(DateTimeFormatter.ISO_DATE_TIME), BoardResource(game.board), game.state.playStatus, game.state.result)
  }
}

case class GameResource(id: String, createdAt: String, board: BoardResource, state: String, result: String)

object BoardResource {
  def apply(board: Board): BoardResource = {
    BoardResource(board.dimensions, board.totalBombs, CellResourceFactory.from(board))
  }
}

case class BoardResource(dimensions: Dimensions, totalBombs: Int, cells: Seq[CellResource])

object CellResourceFactory {
  def from(board: Board): Seq[CellResource] = {
    board.cells.toSeq.map(from(board, _)).sorted
  }

  def from(board: Board, cell: Cell): CellResource = {
    CellResource(cell.row, cell.column, cell.hasBomb, cell.visibility, cell.content)
  }
}

case class CellResource(row: Int, column: Int, hasBomb: Boolean = false, visibility: Visibility, value: Option[String] = None) extends Ordered[CellResource] {
  // https://stackoverflow.com/a/19348339/545273
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: CellResource): Int = {
    (this.row, this.column) compare (that.row, that.column)
  }

  def mapsTo(cell: Cell): Boolean = {
    row == cell.row &&
      column == cell.column &&
      hasBomb == cell.hasBomb &&
      visibility == cell.visibility &&
      value == cell.content
  }
}


