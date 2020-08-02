package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import CellsMatchers._

class CellMarkTest extends AnyWordSpec with TestObjects with Matchers {
  "Cell" when {
    "advance mark" should {
      "get marked" in {
        val cell = Cell(coordinatesX1Y1, CellContent.Bomb, Visibility.Hidden, mark = None)

        val flaggedCell = cell.advanceMark
        flaggedCell should beMarkedWithFlag

        val questionCell = flaggedCell.advanceMark
        questionCell should beMarkedWithQuestion

        val notMarkedCell = questionCell.advanceMark
        notMarkedCell should notBeMarked
      }
    }
  }
}
