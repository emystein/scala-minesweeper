package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

case class Cells(all: Set[Cell]) {
  val empty: Set[Cell] = all.filter(_.content.isEmpty)
  val withBomb: Set[Cell] = all.filter(_.content.isDefined)
  val hidden = CellFilters(all.filter(_.visibility == Hidden))
  val revealed = CellFilters(all.filter(_.visibility == Shown))

  def toSeq: Seq[Cell] = all.toSeq

  def map[B](f: Cell => B): Set[B] = {
    for {
      x <- all
    } yield f(x)
  }
}

case class CellFilters(all: Set[Cell]) {
  def empty(): Set[Cell] = {
    all.filter(_.content.isEmpty)
  }

  def withBomb(): Set[Cell] = {
    all.filter(_.content.isDefined)
  }
}

