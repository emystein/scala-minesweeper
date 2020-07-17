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
        cells.hidden shouldBe HiddenCells(Set(notRevealedEmptyCell, notRevealedCellWithBomb))
        cells.revealed shouldBe RevealedCells(Set(revealedEmptyCell, revealedCellWithBomb))
      }
    }
  }

  "HiddenCells" when {
    "created" should {
      "discriminate empty, bombs" in {
        val hiddenCells = HiddenCells.of(allCells)
        hiddenCells.empty shouldBe Set(notRevealedEmptyCell)
        hiddenCells.withBomb shouldBe Set(notRevealedCellWithBomb)
      }
    }
  }

  "RevealedCells" when {
    "created" should {
      "discriminate empty, bombs" in {
        val revealed = RevealedCells.of(Set(revealedEmptyCell, revealedCellWithBomb))
        revealed.empty shouldBe Set(revealedEmptyCell)
        revealed.withBomb shouldBe Set(revealedCellWithBomb)
      }
    }
  }
}
