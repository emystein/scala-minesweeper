package ar.com.flow.minesweeper

import java.util.Date

import scala.collection.mutable.ArrayBuffer

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt, BoardResource(game.board.totalRows, game.board.totalColumns, game.board.totalBombs, CellResourceFactory.from(game.board.cells)), game.state, game.result)
  }
}

case class GameResource(id: String, createdAt: Date, board: BoardResource, state: String, result: String)

case class BoardResource(totalRows: Int, totalColumns: Int, totalBombs: Int, cells: ArrayBuffer[ArrayBuffer[CellResource]])

object CellResourceFactory {
  def from(cells: ArrayBuffer[ArrayBuffer[Cell]]): ArrayBuffer[ArrayBuffer[CellResource]] = {
    for {
      rows <- cells
    } yield for {
      cell <- rows
    } yield {
      CellResource(cell.row, cell.column, cell.hasBomb, cell.numberOfAdjacentBombs, cell.isRevealed, cell.value)
    }
  }
}

case class CellResource(row: Int, column: Int, hasBomb: Boolean = false, numberOfAdjacentBombs: Int = 0, isRevealed: Boolean = false, value: String = "") {
  def mapsTo(cell: Cell): Boolean = {
    row == cell.row &&
      column == cell.column &&
      hasBomb == cell.hasBomb &&
      numberOfAdjacentBombs == cell.numberOfAdjacentBombs &&
      isRevealed == cell.isRevealed &&
      value == cell.value
  }
}


