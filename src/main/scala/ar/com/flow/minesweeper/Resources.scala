package ar.com.flow.minesweeper

import java.util.Date

import scala.collection.mutable.Map

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt, BoardResource(game.board.totalRows, game.board.totalColumns, game.board.totalBombs, CellResourceFactory.from(game.board.cells)), game.state, game.result)
  }
}

case class GameResource(id: String, createdAt: Date, board: BoardResource, state: String, result: String)

case class BoardResource(totalRows: Int, totalColumns: Int, totalBombs: Int, cells: Seq[CellResource])

object CellResourceFactory {
  def from(cells: Map[(Int, Int), Cell]): Seq[CellResource] = {
    cells.values.map(from(_)).toSeq.sorted
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


