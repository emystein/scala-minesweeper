package ar.com.flow.minesweeper

case class Cell(coordinates: CartesianCoordinates, hasBomb: Boolean = false, isRevealed: Boolean = false, value: String = "") extends Ordered[Cell] {
  // https://stackoverflow.com/a/19348339/545273
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: Cell): Int = {
    (this.coordinates.x, this.coordinates.y) compare (that.coordinates.x, that.coordinates.y)
  }

  def row: Int = coordinates.x
  def column: Int = coordinates.y
}

//case class EmptyCell(override val row: Int, override val column: Int, override val numberOfAdjacentBombs: Int = 0, override val isRevealed: Boolean = false, override val value: String = "") extends Cell(row, column, false, numberOfAdjacentBombs, isRevealed, value )
//case class BombCell(override val row: Int, override val column: Int, override val numberOfAdjacentBombs: Int = 0, override val isRevealed: Boolean = false, override val value: String = "") extends Cell(row, column, true, numberOfAdjacentBombs, isRevealed, value )

object CellValue {
  val empty: String = ""
  val flag: String = "f"
  val question: String = "?"
}
