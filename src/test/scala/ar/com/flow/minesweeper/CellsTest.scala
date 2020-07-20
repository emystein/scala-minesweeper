package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellsTest extends AnyWordSpec with TestCells with Matchers {
  "Cells" when {
    "created" should {
      "discriminate empty, bombs, revealed cells" in {
        val cells = Cells(allCells)

        cells.all shouldBe allCells
        cells.empty shouldBe Set(notRevealedEmptyCell, revealedEmptyCell)
        cells.withBomb shouldBe Set(notRevealedCellWithBomb, revealedCellWithBomb)
        cells.hidden shouldBe CellFilters(Set(notRevealedEmptyCell, notRevealedCellWithBomb))
        cells.revealed shouldBe CellFilters(Set(revealedEmptyCell, revealedCellWithBomb))
      }
    }
  }
}
