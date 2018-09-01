package ar.com.flow.minesweeper

import java.util.Date

import scala.collection.mutable.ArrayBuffer

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt, BoardResource(game.board.totalRows, game.board.totalColumns, game.board.totalBombs, game.board.cells), game.state, game.result)
  }
}
case class GameResource(val id: String, val createdAt: Date, val board: BoardResource, val state: String, val result: String)
case class BoardResource(val totalRows: Int, val totalColumns: Int, val totalBombs: Int, val cells: ArrayBuffer[ArrayBuffer[Cell]])
// TODO make numberOfAdjacentBombs a val
case class Cell(val row: Int, val column: Int, val hasBomb: Boolean = false, numberOfAdjacentBombs: Int, isRevealed: Boolean = false, value: String = "")

object CellValue {
  val empty: String = ""
  val flag: String = "f"
  val question: String = "?"
}

