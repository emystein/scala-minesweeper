package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

case class Cells(all: Iterable[Cell]) {
  val empty: Iterable[Cell] = all.filter(_.content == CellContent.Empty)
  val withBomb: Iterable[Cell] = all.filter(_.content == CellContent.Bomb)
  val hidden = CellFilters(all.filter(_.visibility == Hidden))
  val revealed = CellFilters(all.filter(_.visibility == Shown))
}

case class CellFilters(all: Iterable[Cell]) {
  def empty(): Iterable[Cell] = {
    all.filter(_.content == CellContent.Empty)
  }

  def withBomb(): Iterable[Cell] = {
    all.filter(_.content == CellContent.Bomb)
  }
}