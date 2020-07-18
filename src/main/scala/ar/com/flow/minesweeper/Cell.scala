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

case class Cell(coordinates: CartesianCoordinates, hasBomb: Boolean = false, visibility: CellVisibility = CellVisibility.Hidden, content: Option[String] = None) extends Ordered[Cell] {
  // https://stackoverflow.com/a/19348339/545273
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: Cell): Int = {
    (this.coordinates.x, this.coordinates.y) compare (that.coordinates.x, that.coordinates.y)
  }

  def row: Int = coordinates.x
  def column: Int = coordinates.y
}

sealed abstract class CellVisibility extends Product with Serializable

object CellVisibility {
  final case object Hidden extends CellVisibility
  final case object Shown extends CellVisibility

  def apply(shown: Boolean): CellVisibility =  if (shown) Shown else Hidden
}

class EmptyCell(override val coordinates: CartesianCoordinates, override val visibility: CellVisibility = CellVisibility.Hidden, override val content: Option[String] = None) extends Cell(coordinates, false, visibility, content)

class BombCell(override val coordinates: CartesianCoordinates, override val visibility: CellVisibility = CellVisibility.Hidden, override val content: Option[String] = None) extends Cell(coordinates, true, visibility, content)

object CellContent {
  val empty: String = ""
  val flag: String = "f"
  val question: String = "?"
}
