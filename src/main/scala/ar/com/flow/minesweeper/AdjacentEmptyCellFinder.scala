package ar.com.flow.minesweeper

class AdjacentEmptyCellFinder(board: Board) {
  private val cellLocationContext = new CellLocationContext(board)

  def traverseEmptyAdjacentCells(cell: Cell, previouslyTraversed: Set[Cell] = Set.empty): Set[Cell] = {
    val adjacentCells = adjacentCellsOf(cell).toSet -- previouslyTraversed

    adjacentCells.filter(!_.hasBomb)
      .foldLeft(previouslyTraversed + cell)((traversed, adjacent) => traverseEmptyAdjacentCells(adjacent, traversed))
  }

  private def adjacentCellsOf(cell: Cell): Seq[Cell] = {
    cellLocationContext.neighboursOf(cell).map(board.getCell)
  }
}
