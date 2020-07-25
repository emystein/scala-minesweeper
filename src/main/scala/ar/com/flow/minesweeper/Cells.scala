package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

case class Cells(all: Iterable[Cell]) {
  val empty: Iterable[Cell] = all.filter(_.content == CellContent.Empty)
  val withBomb: Iterable[Cell] = all.filter(_.content == CellContent.Bomb)
  val hidden = CellContentFilters(all.filter(_.visibility == Hidden))
  val revealed = CellContentFilters(all.filter(_.visibility == Shown))
}

case class CellContentFilters(all: Iterable[Cell]) {
  def empty(): Iterable[Cell] = {
    all.filter(_.content == CellContent.Empty)
  }

  def withBomb(): Iterable[Cell] = {
    all.filter(_.content == CellContent.Bomb)
  }
}