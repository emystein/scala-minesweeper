package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

object Cells {
  def apply(source: Set[Cell]): Cells = {
    val empty: Set[Cell] = source.filter(_.content.isEmpty)
    val withBomb: Set[Cell] = source.filter(_.content.isDefined)
    val hidden = HiddenCells.of(source)
    val revealed = RevealedCells.of(source)
    new Cells(empty, withBomb, hidden, revealed)
  }
}

trait CellSet {
  val cells: Set[Cell]

  def empty(): Set[Cell] = {
    cells.filter(_.content.isEmpty)
  }

  def withBomb(): Set[Cell] = {
    cells.filter(_.content.isDefined)
  }
}

object HiddenCells {
  def of(source: Set[Cell]): HiddenCells = {
    new HiddenCells(source.filter(_.visibility == Hidden))
  }
}

case class HiddenCells(cells: Set[Cell]) extends CellSet

object RevealedCells {
  def of(source: Set[Cell]): RevealedCells = {
    new RevealedCells(source.filter(_.visibility == Shown))
  }
}

case class RevealedCells(cells: Set[Cell]) extends CellSet

case class Cells(empty: Set[Cell], withBomb: Set[Cell], hidden: HiddenCells, revealed: RevealedCells) {
  def all: Set[Cell] = empty ++ withBomb
  def notRevealedEmpty: Set[Cell] = empty -- revealed.empty

  def toSeq: Seq[Cell] = all.toSeq

  def map[B](f: Cell => B): Set[B] = {
    for {
      x <- all
    } yield f(x)
  }
}
