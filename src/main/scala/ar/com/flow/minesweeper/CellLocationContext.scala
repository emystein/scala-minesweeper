package ar.com.flow.minesweeper

class CellLocationContext(totalRows: Int, totalColumns: Int) {
  def neighboursOf(cell: Cell): Seq[(Int, Int)] = {
    neighboursOf(cell.row, cell.column)
  }

  def neighboursOf(row: Int, column: Int): Seq[(Int, Int)] = {
    for {
      x <- row - 1 to row + 1
      y <- column - 1 to column + 1
      if (x > 0 && x <= totalRows) && (y > 0 && y <= totalColumns) && (x != row || y != column)
    } yield (x, y)
  }
}
