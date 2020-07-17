package ar.com.flow.minesweeper

trait TestCells {
  val notRevealedEmptyCell = Cell(CartesianCoordinates(1, 1), isRevealed = false, hasBomb = false)
  val notRevealedCellWithBomb = Cell(CartesianCoordinates(1, 1), isRevealed = false, hasBomb = true)
  val revealedEmptyCell = Cell(CartesianCoordinates(1, 1), isRevealed = true)
  val revealedCellWithBomb = Cell(CartesianCoordinates(1, 1), isRevealed = true, hasBomb = true)

  val allCells = Set(notRevealedEmptyCell, notRevealedCellWithBomb, revealedEmptyCell, revealedCellWithBomb)
}
