package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellsTest extends AnyWordSpec with Matchers {
  "Cells" when {
    "created with empty and bombs" should {
      "discriminate empty and bombs" in {
        val emptyCell = Cell(1, 1, hasBomb = false)
        val bombCell = Cell(1, 2, hasBomb = true)

        val cells: Cells = Cells(Set(emptyCell, bombCell))

        cells.empty shouldBe Set(emptyCell)
        cells.withBomb shouldBe Set(bombCell)
      }
      "aggregate all" in {
        val emptyCell = Cell(1, 1, hasBomb = false)
        val bombCell = Cell(1, 2, hasBomb = true)
        val allCells = Set(emptyCell, bombCell)

        val cells: Cells = Cells(allCells)

        cells.all shouldBe allCells
      }
      "have remaining bombs to be discovered" in {
        val notRevealedEmptyCell = Cell(1, 1, isRevealed = false, hasBomb = false)
        val revealedEmptyCell = Cell(1, 1, isRevealed = true)
        val revealedCellWithBomb = Cell(1, 1, isRevealed = true, hasBomb = true)

        val cells: Cells = Cells(Set(notRevealedEmptyCell, revealedEmptyCell, revealedCellWithBomb))

        cells.notRevealedEmpty shouldBe Set(notRevealedEmptyCell)
      }
    }
    "created with revealed cells" should {
      "discriminate revealed" in {
        val notRevealedCell = Cell(1, 1, isRevealed = false)
        val revealedEmptyCell = Cell(1, 1, isRevealed = true)
        val revealedCellWithBomb = Cell(1, 1, isRevealed = true, hasBomb = true)

        val cells: Cells = Cells(Set(notRevealedCell, revealedEmptyCell, revealedCellWithBomb))

        cells.revealedEmpty shouldBe Set(revealedEmptyCell)
        cells.revealedWithBomb shouldBe Set(revealedCellWithBomb)
      }
    }
  }
}
