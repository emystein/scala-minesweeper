package ar.com.flow.minesweeper

import java.util.Date

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt, BoardResource(game.board), game.state, game.result)
  }
}

case class GameResource(id: String, createdAt: Date, board: BoardResource, state: String, result: String)

object BoardResource {
  def apply(board: Board): BoardResource = {
    BoardResource(board.totalRows, board.totalColumns, board.totalBombs, CellResourceFactory.from(board.cells))
  }
}

case class BoardResource(totalRows: Int, totalColumns: Int, totalBombs: Int, cells: Seq[CellResource])

object CellResourceFactory {
  def from(cells: Seq[Cell]): Seq[CellResource] = {
    cells.map(from(_)).sorted
  }

  def from(cell: Cell): CellResource = {
    CellResource(cell.row, cell.column, cell.hasBomb, cell.numberOfAdjacentBombs, cell.isRevealed, cell.value)
  }
}

case class CellResource(row: Int, column: Int, hasBomb: Boolean = false, numberOfAdjacentBombs: Int = 0, isRevealed: Boolean = false, value: String = "") extends Ordered[CellResource] {
  // https://stackoverflow.com/a/19348339/545273
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: CellResource): Int = {
    (this.row, this.column) compare (that.row, that.column)
  }

  def mapsTo(cell: Cell): Boolean = {
    row == cell.row &&
      column == cell.column &&
      hasBomb == cell.hasBomb &&
      numberOfAdjacentBombs == cell.numberOfAdjacentBombs &&
      isRevealed == cell.isRevealed &&
      value == cell.value
  }
}


