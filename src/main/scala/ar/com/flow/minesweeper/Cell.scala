package ar.com.flow.minesweeper

case class Cell(row: Int, column: Int, hasBomb: Boolean = false, numberOfAdjacentBombs: Int = 0, isRevealed: Boolean = false, value: String = "")

object CellValue {
  val empty: String = ""
  val flag: String = "f"
  val question: String = "?"
}
