package ar.com.flow.minesweeper

object Cell {
  def apply(coordinates: CartesianCoordinates, hasBomb: Boolean): Cell = {
    if (hasBomb) {
      new BombCell(coordinates)
    } else {
      new EmptyCell(coordinates)
    }
  }
}

case class Cell(coordinates: CartesianCoordinates, hasBomb: Boolean = false, visibility: CellValueVisibility = CellValueVisibility.Hidden, value: String = "") extends Ordered[Cell] {
  // https://stackoverflow.com/a/19348339/545273
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: Cell): Int = {
    (this.coordinates.x, this.coordinates.y) compare (that.coordinates.x, that.coordinates.y)
  }

  def row: Int = coordinates.x
  def column: Int = coordinates.y
}

sealed abstract class CellValueVisibility extends Product with Serializable

object CellValueVisibility {
  final case object Hidden extends CellValueVisibility
  final case object Shown extends CellValueVisibility

  def apply(shown: Boolean): CellValueVisibility = if (shown) { Shown } else { Hidden}

  def unapply(visibility: CellValueVisibility): Boolean = visibility match {
    case Shown => true
    case Hidden => false
  }
}

class EmptyCell(override val coordinates: CartesianCoordinates, override val visibility: CellValueVisibility = CellValueVisibility.Hidden, override val value: String = "") extends Cell(coordinates, false, visibility, value)

class BombCell(override val coordinates: CartesianCoordinates, override val visibility: CellValueVisibility = CellValueVisibility.Hidden, override val value: String = "") extends Cell(coordinates, true, visibility, value )

object CellValue {
  val empty: String = ""
  val flag: String = "f"
  val question: String = "?"
}
