package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

object Cells {
  def apply(source: Set[Cell]): Cells = {
    val empty: Set[Cell] = source.filter(_.content.isEmpty)
    val withBomb: Set[Cell] = source.filter(_.content.isDefined)
    val hidden = CellSet(source.filter(_.visibility == Hidden))
    val revealed = CellSet(source.filter(_.visibility == Shown))
    new Cells(empty, withBomb, hidden, revealed)
  }
}

case class CellSet(cells: Set[Cell]) {
  def empty(): Set[Cell] = {
    cells.filter(_.content.isEmpty)
  }

  def withBomb(): Set[Cell] = {
    cells.filter(_.content.isDefined)
  }
}

case class Cells(empty: Set[Cell], withBomb: Set[Cell], hidden: CellSet, revealed: CellSet) {
  def all: Set[Cell] = empty ++ withBomb
  def notRevealedEmpty: Set[Cell] = empty -- revealed.empty

  def toSeq: Seq[Cell] = all.toSeq

  def map[B](f: Cell => B): Set[B] = {
    for {
      x <- all
    } yield f(x)
  }
}
