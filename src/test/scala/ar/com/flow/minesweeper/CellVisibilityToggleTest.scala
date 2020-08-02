package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import CellsMatchers._

class CellVisibilityToggleTest extends AnyWordSpec with TestObjects with Matchers {
  "Hidden Cell" when {
    "reveal" should {
      "be revealed" in {
        val hiddenCell = Cell(coordinatesX1Y1, visibility = Hidden)

        hiddenCell.reveal should beRevealed
      }
    }
  }
  "Revealed Cell" when {
    "reveal" should {
      "keep revealed" in {
        val shownCell = Cell(coordinatesX1Y1, visibility = Shown)

        shownCell.reveal should beRevealed
      }
    }
  }
}
