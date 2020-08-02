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

  class CellVisibilityMatcher(visibility: Visibility) extends Matcher[Cell] {
    def apply(cell: Cell): MatchResult = {
      MatchResult(
        cell.visibility == visibility,
        s"Cell visibility is $visibility",
        s"Cell visibility is not $visibility",
      )
    }
  }

  class CellAnyMarkMatcher() extends Matcher[Cell] {
    def apply(cell: Cell): MatchResult = {
      MatchResult(
        cell.mark.isDefined,
        s"Cell is marked",
        s"Cell is not marked"
      )
    }
  }

  class CellMarkMatcher(mark: Option[CellMark]) extends Matcher[Cell] {
    def apply(cell: Cell): MatchResult = {
      MatchResult(
        cell.mark == mark,
        s"Cell mark is $mark",
        s"Cell mark is not $mark",
      )
    }
  }

  def haveAdjacentEmptySpaceRevealed = new AdjacentVisibilityMatcher(Visibility.Shown)
  def allBeHidden = new AllCellsHaveVisibilityMatcher(Visibility.Hidden)
  def allBeRevealed = new AllCellsHaveVisibilityMatcher(Visibility.Shown)
  def beRevealed = new CellVisibilityMatcher(Visibility.Shown)
  def beMarked = new CellAnyMarkMatcher()
  def notBeMarked = new CellMarkMatcher(None)
  def beMarkedWithFlag = new CellMarkMatcher(Some(CellMark.Flag))
  def beMarkedWithQuestion = new CellMarkMatcher(Some(CellMark.Question))
}

// Make them easy to import with:
// import CellsMatchers._
object CellsMatchers extends CellsMatchers
