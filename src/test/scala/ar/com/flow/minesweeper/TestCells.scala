package ar.com.flow.minesweeper

trait TestCells {
  val notRevealedEmptyCell = new EmptyCell(CartesianCoordinates(1, 1), Visibility.Hidden)
  val notRevealedCellWithBomb = new BombCell(CartesianCoordinates(1, 1), Visibility.Hidden)
  val revealedEmptyCell = new EmptyCell(CartesianCoordinates(1, 1), Visibility.Shown)
  val revealedCellWithBomb = new BombCell(CartesianCoordinates(1, 1), Visibility.Shown)

  val allCells = Set(notRevealedEmptyCell, notRevealedCellWithBomb, revealedEmptyCell, revealedCellWithBomb)
}
