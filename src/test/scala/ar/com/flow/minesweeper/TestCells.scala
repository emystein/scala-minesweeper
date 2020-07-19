package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.CellContent.Bomb

trait TestCells {
  val board: Board = Board(Dimensions(3, 3), totalBombs = 3)

  val notRevealedEmptyCell = new Cell(CartesianCoordinates(1, 1), content = None, Visibility.Hidden, mark = None, board)
  val notRevealedCellWithBomb = Cell(CartesianCoordinates(1, 1), content = Some(Bomb), Visibility.Hidden, mark = None, board)
  val revealedEmptyCell = Cell(CartesianCoordinates(1, 1), content = None, Visibility.Shown, mark = None, board)
  val revealedCellWithBomb = Cell(CartesianCoordinates(1, 1), content = Some(Bomb), Visibility.Shown, mark = None, board)

  val allCells = Set(notRevealedEmptyCell, notRevealedCellWithBomb, revealedEmptyCell, revealedCellWithBomb)
}
