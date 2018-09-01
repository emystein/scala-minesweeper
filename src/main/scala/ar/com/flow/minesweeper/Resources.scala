package ar.com.flow.minesweeper

import java.util.Date

import scala.collection.mutable.ArrayBuffer

case class NewGameRequestBody(rows: Int, columns: Int, bombs: Int)

object GameResource {
  def from(game: Game): GameResource = {
    GameResource(game.id, game.createdAt, BoardResource(game.board.totalRows, game.board.totalColumns, game.board.totalBombs, game.board.cells))
  }
}
case class GameResource(val id: String, val createdAt: Date, val board: BoardResource)
case class BoardResource(val totalRows: Int, val totalColumns: Int, val totalBombs: Int, val cells: ArrayBuffer[ArrayBuffer[Cell]])
// TODO make numberOfAdjacentBombs a val
case class Cell(val row: Int, val column: Int, val hasBomb: Boolean = false, numberOfAdjacentBombs: Int, isRevealed: Boolean = false, value: CellValue.Value = CellValue.NONE)

object CellValue extends Enumeration {
  val value = Value
  val NONE, BOMB, FLAG, QUESTION, ADJACENT_1, ADJACENT_2, ADJACENT_3, ADJACENT_4, ADJACENT_5, ADJACENT_6, ADJACENT_7, ADJACENT_8 = Value
}

