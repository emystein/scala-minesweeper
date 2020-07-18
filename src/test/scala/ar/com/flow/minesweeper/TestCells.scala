package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.CellContent.Bomb

trait TestCells {
  val notRevealedEmptyCell = Cell(CartesianCoordinates(1, 1), content = None, Visibility.Hidden)
  val notRevealedCellWithBomb = Cell(CartesianCoordinates(1, 1), content = Some(Bomb), Visibility.Hidden)
  val revealedEmptyCell = Cell(CartesianCoordinates(1, 1), content = None, Visibility.Shown)
  val revealedCellWithBomb = Cell(CartesianCoordinates(1, 1), content = Some(Bomb), Visibility.Shown)

  val allCells = Set(notRevealedEmptyCell, notRevealedCellWithBomb, revealedEmptyCell, revealedCellWithBomb)
}
