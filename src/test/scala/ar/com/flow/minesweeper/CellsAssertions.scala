package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}
import org.scalatest.matchers.should.Matchers

trait CellsAssertions extends Matchers {
  def allCellsShouldBeRevealed(cells: Seq[Cell], visibility: Visibility = Shown)(implicit game: Game) =
    cells.foreach(cell => game.board.getCell(cell.coordinates).visibility shouldBe visibility)

  def allCellsShouldBeHidden(cells: Seq[Cell])(implicit game: Game) =
    allCellsShouldBeRevealed(cells, Hidden)
}
