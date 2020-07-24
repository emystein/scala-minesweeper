package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.Hidden

object Cell {
  def apply(coordinates: CartesianCoordinates, cellState: CellState, board: Board): Cell = {
    new Cell(coordinates, cellState.content, cellState.visibility, cellState.mark, board)
  }
}

case class Cell(coordinates: CartesianCoordinates,
                content: CellContent = CellContent.Empty,
                visibility: Visibility = Visibility.Hidden,
                mark: Option[CellMark] = None, board: Board) extends Ordered[Cell] {

  def adjacentCells: Set[Cell] = board.adjacentOf(coordinates).map(board.cellAt)

  def adjacentEmptySpace(previouslyTraversed: Set[Cell] = Set.empty): Set[Cell] = {
    (adjacentCells -- previouslyTraversed).filter(_.content == CellContent.Empty)
      .foldLeft(previouslyTraversed + this)((traversed, adjacent) => adjacent.adjacentEmptySpace(traversed))
  }
  
  // https://stackoverflow.com/a/19348339/545273

  override def compare(that: Cell): Int = {
    this.coordinates compare that.coordinates
  }
}

case class CellState(content: CellContent = CellContent.Empty,
                     visibility: Visibility = Hidden,
                     mark: Option[CellMark] = None)

abstract class CellContent extends Product with Serializable

object CellContent {
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
