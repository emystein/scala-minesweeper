package ar.com.flow.minesweeper

trait TestObjects {
  val coordinatesX1Y1 = CartesianCoordinates(1, 1)
  val coordinatesX1Y2 = CartesianCoordinates(1, 2)
  val coordinatesX2Y1 = CartesianCoordinates(2, 1)
  val coordinatesX2Y2 = CartesianCoordinates(2, 2)

  val notRevealedEmptyCell = Cell(coordinatesX1Y1, CellContent.Empty, Visibility.Hidden, mark = None)
  val notRevealedCellWithBomb = Cell(coordinatesX1Y1, CellContent.Bomb, Visibility.Hidden, mark = None)
  val revealedEmptyCell = Cell(coordinatesX1Y1, CellContent.Empty, Visibility.Revealed, mark = None)
  val revealedCellWithBomb = Cell(coordinatesX1Y1, CellContent.Bomb, Visibility.Revealed, mark = None)

  val allCells = Set(notRevealedEmptyCell, notRevealedCellWithBomb, revealedEmptyCell, revealedCellWithBomb)

  val board3x3: Board = Board(Dimensions(3, 3), totalBombs = 3)

  val game = Game(totalRows = 3, totalColumns = 3, totalBombs = 2)
}
