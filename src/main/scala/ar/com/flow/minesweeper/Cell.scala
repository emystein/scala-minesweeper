package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.CellContent.Bomb

//object Cell {
//  def apply(coordinates: CartesianCoordinates, hasBomb: Boolean): Cell = {
//    if (hasBomb) {
//      new BombCell(coordinates)
//    } else {
//      new EmptyCell(coordinates)
//    }
//  }
//}

case class Cell(coordinates: CartesianCoordinates, content: Option[CellContent] = None, visibility: Visibility = Visibility.Hidden, mark: Option[String] = None) extends Ordered[Cell] {
  // https://stackoverflow.com/a/19348339/545273

  override def compare(that: Cell): Int = {
    this.coordinates compare that.coordinates
  }

//  val content: Option[CellContent] = CellContent.apply(hasBomb)
}

//class EmptyCell(override val coordinates: CartesianCoordinates, override val visibility: Visibility = Visibility.Hidden, override val mark: Option[String] = None) extends Cell(coordinates, None, visibility, mark)
//
//class BombCell(override val coordinates: CartesianCoordinates, override val visibility: Visibility = Visibility.Hidden, override val mark: Option[String] = None) extends Cell(coordinates, Some(Bomb), visibility, mark)

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
