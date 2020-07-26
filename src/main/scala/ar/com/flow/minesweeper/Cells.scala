package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}

object Cells {
  def apply(board: Board): Cells = {
    new Cells(board.contentByCoordinates, board.visibilityByCoordinates, board.markByCoordinates, board)
  }
}

case class Cells(contentByCoordinates: Map[CartesianCoordinates, CellContent],
                 visibilityByCoordinates: Map[CartesianCoordinates, Visibility],
                 markByCoordinates: Map[CartesianCoordinates, Option[CellMark]],
                 board: Board) {

  val all: Iterable[Cell] = for {
    (coordinates, content) <- contentByCoordinates
    visibility = visibilityByCoordinates(coordinates)
    mark = markByCoordinates(coordinates)
  } yield {
    Cell(coordinates, content, visibility, mark, board = Some(board))
  }
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