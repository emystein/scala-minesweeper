package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellMarkTest extends AnyWordSpec with Matchers {
  "Hidden Cell" when {
    "mark" should {
      "get flagged" in {
        val cell = Cell(CartesianCoordinates(1, 1), CellContent.Bomb, Visibility.Hidden, mark = None)

        val flaggedCell = cell.advanceMark

        flaggedCell.mark shouldBe Some(CellMark.Flag)
      }
    }
  }
  "Flagged Cell" when {
    "mark" should {
      "marked with question" in {
        val flaggedCell = Cell(CartesianCoordinates(1, 1), CellContent.Bomb, Visibility.Hidden, mark = Some(CellMark.Flag))

        val questionCell = flaggedCell.advanceMark

        questionCell.mark shouldBe Some(CellMark.Question)
      }
    }
  }
  "Question Cell" when {
    "mark" should {
      "remove mark" in {
        val questionCell = Cell(CartesianCoordinates(1, 1), CellContent.Bomb, Visibility.Hidden, mark = Some(CellMark.Question))

        questionCell.advanceMark.mark shouldBe None
      }
    }
  }
}
