package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

object Cells {
  def apply[T <: Cell](source: Set[T]): Cells[T] = {
    val empty: Set[T] = source.filter(_.content.isEmpty)
    val withBomb: Set[T] = source.filter(_.content.isDefined)
    val hidden = HiddenCells(source.filter(_.visibility == Hidden))
    val revealed = RevealedCells.of(source)
    new Cells(empty, withBomb, hidden, revealed)
  }
}

trait CellSet[T <: Cell] {
  val cells: Set[T]

  def empty(): Set[T] = {
    cells.filter(_.content.isEmpty)
  }

  def withBomb(): Set[T] = {
    cells.filter(_.content.isDefined)
  }
}

object HiddenCells {
  def of[T <: Cell](source: Set[T]): HiddenCells[T] = {
    new HiddenCells(source.filter(_.visibility == Hidden))
  }
}

case class HiddenCells[T <: Cell](cells: Set[T]) extends CellSet[T]

object RevealedCells {
  def of[T <: Cell](source: Set[T]): RevealedCells[T] = {
    new RevealedCells(source.filter(_.visibility == Shown))
  }
}

case class RevealedCells[T <: Cell](cells: Set[T]) extends CellSet[T]

case class Cells[T <: Cell](empty: Set[T], withBomb: Set[T], hidden: CellSet[T], revealed: CellSet[T]) {
  def all: Set[T] = empty ++ withBomb
  def notRevealedEmpty: Set[T] = empty -- revealed.empty

  def toSeq: Seq[T] = all.toSeq

  def map[B](f: Cell => B): Set[B] = {
    for {
      x <- all
    } yield f(x)
  }
}
