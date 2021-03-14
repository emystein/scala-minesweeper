package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.CellContent.Empty
import ar.com.flow.minesweeper.CellMark.{Flag, Question}
import ar.com.flow.minesweeper.Visibility.{Hidden, Revealed}

case class Cell(coordinates: CartesianCoordinates,
                content: CellContent = Empty,
                visibility: Visibility = Hidden,
                mark: Option[CellMark] = None,
                board: Option[Board] = None) {
  val isEmpty: Boolean = content == Empty
  val isHidden: Boolean = visibility == Hidden
  val isRevealed: Boolean = visibility == Revealed

  def adjacentCells: Set[Cell] = board.map(b => b.adjacentOf(coordinates).map(b.cellAt)).getOrElse(Set())

  def adjacentEmptySpace(previouslyTraversed: Set[Cell] = Set()): Set[Cell] = {
    (adjacentCells -- previouslyTraversed)
      .filter(_.isEmpty)
      .foldLeft(previouslyTraversed + this)((traversed, adjacent) => adjacent.adjacentEmptySpace(traversed))
  }

  def reveal: Cell = copy(visibility = Revealed)

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

  def apply(hasBomb: Boolean): CellContent = if (hasBomb) CellContent.Bomb else Empty
}

sealed abstract class Visibility extends Product with Serializable

object Visibility {
  final case object Hidden extends Visibility
  final case object Revealed extends Visibility
}

sealed abstract class CellMark extends Product with Serializable

object CellMark {
  final case object Flag extends CellMark
  final case object Question extends CellMark
}
