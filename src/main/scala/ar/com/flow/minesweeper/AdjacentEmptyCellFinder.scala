package ar.com.flow.minesweeper

class AdjacentEmptyCellFinder(board: Board) {
  def traverseEmptyAdjacentCells(cell: Cell, previouslyTraversed: Set[Cell] = Set.empty): Set[Cell] = {
    val adjacentCells = board.adjacentCellsOf(cell).toSet -- previouslyTraversed

    adjacentCells.filter(!_.hasBomb)
      .foldLeft(previouslyTraversed + cell)((traversed, adjacent) => traverseEmptyAdjacentCells(adjacent, traversed))
  }
}
