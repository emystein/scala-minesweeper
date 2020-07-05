package ar.com.flow.minesweeper

class CellLocationContext(dimensions: Dimensions) {
  def neighboursOf(cell: Cell): Seq[Board.Coordinates] = {
    neighboursOf(cell.row, cell.column)
  }

  def neighboursOf(row: Int, column: Int): Seq[Board.Coordinates] = {
    for {
      x <- row - 1 to row + 1
      y <- column - 1 to column + 1
      if (x > 0 && x <= dimensions.rows) && (y > 0 && y <= dimensions.columns) && (x != row || y != column)
    } yield (x, y)
  }
}
