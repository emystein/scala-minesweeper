package ar.com.flow.minesweeper

case class Cell(coordinates: CartesianCoordinates, content: Option[CellContent] = None, visibility: Visibility = Visibility.Hidden, mark: Option[String] = None) extends Ordered[Cell] {
  // https://stackoverflow.com/a/19348339/545273

  override def compare(that: Cell): Int = {
    this.coordinates compare that.coordinates
  }
}

abstract class CellContent extends Product with Serializable

object CellContent {
  def apply(hasBomb: Boolean): Option[CellContent] =  if (hasBomb) Some(Bomb) else None

  final case object Bomb extends CellContent
}

object CellMark {
  val empty: String = ""
  val flag: String = "f"
  val question: String = "?"
}
