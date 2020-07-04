package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers

trait CellsAssertions extends Matchers {
  def allCellsShouldBeRevealed(cells: Seq[Cell], isRevealed: Boolean = true)(implicit game: Game) =
    cells.foreach(cell => game.board.getCell(cell.row, cell.column).isRevealed shouldBe isRevealed)

  def allCellsShouldBeHidden(cells: Seq[Cell])(implicit game: Game) =
    allCellsShouldBeRevealed(cells, false)
}
