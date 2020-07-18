package ar.com.flow.minesweeper

trait TestCells {
  val notRevealedEmptyCell = new EmptyCell(CartesianCoordinates(1, 1), CellValueVisibility.Hidden)
  val notRevealedCellWithBomb = new BombCell(CartesianCoordinates(1, 1), CellValueVisibility.Hidden)
  val revealedEmptyCell = new EmptyCell(CartesianCoordinates(1, 1), CellValueVisibility.Shown)
  val revealedCellWithBomb = new BombCell(CartesianCoordinates(1, 1), CellValueVisibility.Shown)

  val allCells = Set(notRevealedEmptyCell, notRevealedCellWithBomb, revealedEmptyCell, revealedCellWithBomb)
}
