package ar.com.flow.minesweeper

import java.util.Date

import scala.collection.mutable.ArrayBuffer

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt, BoardResource(game.board.totalRows, game.board.totalColumns, game.board.totalBombs, game.board.cells), game.state, game.result)
  }
}
case class GameResource(id: String, createdAt: Date, board: BoardResource, state: String, result: String)
case class BoardResource(totalRows: Int, totalColumns: Int, totalBombs: Int, cells: ArrayBuffer[ArrayBuffer[Cell]])
case class Cell(row: Int, column: Int, hasBomb: Boolean = false, numberOfAdjacentBombs: Int, isRevealed: Boolean = false, value: String = "")

object CellValue {
  val empty: String = ""
  val flag: String = "f"
  val question: String = "?"
}

