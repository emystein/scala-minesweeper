package ar.com.flow.minesweeper

object Cells {
  def apply(source: Set[Cell]): Cells = {
    val empty: Set[Cell] = source.filter(!_.hasBomb)
    val withBomb: Set[Cell] = source.filter(_.hasBomb)
    val revealed: Set[Cell] = source.filter(_.isRevealed)
    new Cells(empty, withBomb, revealed)
  }
}

case class Cells(empty: Set[Cell], withBomb: Set[Cell], revealed: Set[Cell]) {
  def all: Set[Cell] = empty ++ withBomb
  def revealedEmpty: Set[Cell] = revealed.filter(!_.hasBomb)
  def revealedWithBomb: Set[Cell] = revealed.filter(_.hasBomb)
  def notRevealedEmpty: Set[Cell] = empty -- revealedEmpty

  def toSeq: Seq[Cell] = all.toSeq

  def map[B](f: Cell => B): Set[B] = {
    for {
      x <- all
    } yield f(x)
  }
}
