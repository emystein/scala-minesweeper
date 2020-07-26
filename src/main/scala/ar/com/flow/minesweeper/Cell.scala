package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.CellMark.{Flag, Question}
import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

case class Cell(coordinates: CartesianCoordinates,
                content: CellContent = CellContent.Empty,
                visibility: Visibility = Hidden,
                mark: Option[CellMark] = None) {
  def toggleVisibility: Cell = {
    if (visibility == Hidden) {
      copy(visibility = Shown)
    } else {
      this
    }
  }

  def advanceMark: Cell = {
    val newMark: Option[CellMark] = mark match {
      case None => Some(Flag)
      case Some(Flag) => Some(Question)
      case Some(Question) => None
    }
    copy(mark = newMark)
  }
}

abstract class CellContent extends Product with Serializable

object CellContent {
  // TODO model using Option?
  final case object Empty extends CellContent
  final case object Bomb extends CellContent

  implicit val booleanToCellContent: Boolean => CellContent =
    hasBomb => if (hasBomb) CellContent.Bomb else CellContent.Empty
}

sealed abstract class Visibility extends Product with Serializable

object Visibility {
  final case object Hidden extends Visibility
  final case object Shown extends Visibility
}

sealed abstract class CellMark extends Product with Serializable

object CellMark {
  final case object Flag extends CellMark
  final case object Question extends CellMark
}
