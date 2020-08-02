package ar.com.flow.minesweeper

import org.scalatest.matchers.{MatchResult, Matcher}

trait CellsMatchers {
  class AdjacentVisibilityMatcher(visibility: Visibility) extends Matcher[Cell] {
    def apply(cell: Cell): MatchResult = {
      MatchResult(
        cell.adjacentEmptySpace().forall(cell => cell.visibility == visibility),
        s"Cell visibility is $visibility",
        s"Cell visibility is not $visibility",
      )
    }
  }

  class AllCellsHaveVisibilityMatcher(visibility: Visibility) extends Matcher[Iterable[Cell]] {
    def apply(cells: Iterable[Cell]): MatchResult = {
      MatchResult(
        cells.forall(cell => cell.visibility == visibility),
        s"Cells visibility is $visibility",
        s"Cells visibility is not $visibility",
      )
    }
  }

  def haveAdjacentEmptySpaceRevealed = new AdjacentVisibilityMatcher(Visibility.Shown)
  def allBeHidden = new AllCellsHaveVisibilityMatcher(Visibility.Hidden)
  def allBeRevealed = new AllCellsHaveVisibilityMatcher(Visibility.Shown)
}

// Make them easy to import with:
// import CellsMatchers._
object CellsMatchers extends CellsMatchers
