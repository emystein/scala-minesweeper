package ar.com.flow.minesweeper

// TODO: Calculate numberOfAdjacentBombs in runtime? It would require a cell to know its neighbours...
case class Cell(row: Int, column: Int, hasBomb: Boolean = false, numberOfAdjacentBombs: Int = 0, isRevealed: Boolean = false, value: String = "") extends Ordered[Cell] {
  // https://stackoverflow.com/a/19348339/545273
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: Cell): Int = {
    (this.row, this.column) compare (that.row, that.column)
  }
}

//case class EmptyCell(override val row: Int, override val column: Int, override val numberOfAdjacentBombs: Int = 0, override val isRevealed: Boolean = false, override val value: String = "") extends Cell(row, column, false, numberOfAdjacentBombs, isRevealed, value )
//case class BombCell(override val row: Int, override val column: Int, override val numberOfAdjacentBombs: Int = 0, override val isRevealed: Boolean = false, override val value: String = "") extends Cell(row, column, true, numberOfAdjacentBombs, isRevealed, value )

object CellValue {
  val empty: String = ""
  val flag: String = "f"
  val question: String = "?"
}
