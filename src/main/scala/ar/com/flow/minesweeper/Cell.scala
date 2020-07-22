package ar.com.flow.minesweeper

object Cell {
  def apply(coordinates: CartesianCoordinates, cellData: CellData, board: Board): Cell = {
    new Cell(coordinates, cellData.content, cellData.visibility, cellData.mark , board)
  }
}

case class Cell(coordinates: CartesianCoordinates,
                content: Option[CellContent] = None,
                visibility: Visibility = Visibility.Hidden,
                mark: Option[String] = None, board: Board) extends Ordered[Cell] {

  val adjacentCells: Set[Cell] = board.cellsAdjacentTo(coordinates).toSet

  def adjacentEmptySpace(previouslyTraversed: Set[Cell] = Set.empty): Set[Cell] = {
    (this.adjacentCells -- previouslyTraversed).filter(_.content.isEmpty)
      .foldLeft(previouslyTraversed + this)((traversed, adjacent) => adjacent.adjacentEmptySpace(traversed))
  }
  
  // https://stackoverflow.com/a/19348339/545273

  override def compare(that: Cell): Int = {
    this.coordinates compare that.coordinates
  }
}

abstract class CellContent extends Product with Serializable

object CellContent {
  def apply(hasBomb: Boolean): Option[CellContent] =  if (hasBomb) Some(Bomb) else None

  final case object Bomb extends CellContent
}

object CellMark {
  val empty: String = ""
  val flag: String = "f"
  val question: String = "?"
}
