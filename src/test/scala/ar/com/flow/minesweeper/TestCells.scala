package ar.com.flow.minesweeper

trait TestCells {
  val board: Board = Board(Dimensions(3, 3), totalBombs = 3)

  val notRevealedEmptyCell = Cell(CartesianCoordinates(1, 1), CellContent.Empty, Visibility.Hidden, mark = None, board)
  val notRevealedCellWithBomb = Cell(CartesianCoordinates(1, 1), CellContent.Bomb, Visibility.Hidden, mark = None, board)
  val revealedEmptyCell = Cell(CartesianCoordinates(1, 1), CellContent.Empty, Visibility.Shown, mark = None, board)
  val revealedCellWithBomb = Cell(CartesianCoordinates(1, 1), CellContent.Bomb, Visibility.Shown, mark = None, board)

  val allCells = Set(notRevealedEmptyCell, notRevealedCellWithBomb, revealedEmptyCell, revealedCellWithBomb)
}
