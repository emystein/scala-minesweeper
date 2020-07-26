package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellVisibilityToggleTest extends AnyWordSpec with Matchers {
  "Hidden Cell" when {
    "toggle visibility" should {
      "be shown" in {
        val hiddenCell = Cell(CartesianCoordinates(1, 1), visibility = Hidden)

        hiddenCell.toggleVisibility.visibility shouldBe Shown
      }
    }
  }
  "Shown Cell" when {
    "toggle visibility" should {
      "be shown" in {
        val shownCell = Cell(CartesianCoordinates(1, 1), visibility = Shown)

        shownCell.toggleVisibility.visibility shouldBe Shown
      }
    }
  }
}
