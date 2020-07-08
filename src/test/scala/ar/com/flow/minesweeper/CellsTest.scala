package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellsTest extends AnyWordSpec with Matchers {
  val notRevealedEmptyCell = Cell(CartesianCoordinates(1, 1), isRevealed = false, hasBomb = false)
  val revealedEmptyCell = Cell(CartesianCoordinates(1, 1), isRevealed = true)
  val revealedCellWithBomb = Cell(CartesianCoordinates(1, 1), isRevealed = true, hasBomb = true)

  val allCells = Set(notRevealedEmptyCell, revealedEmptyCell, revealedCellWithBomb)

  "Cells" when {
    "created" should {
      "discriminate empty, bombs, revealed cells" in {
        val cells = Cells(allCells)

        cells.all shouldBe allCells
        cells.empty shouldBe Set(notRevealedEmptyCell, revealedEmptyCell)
        cells.withBomb shouldBe Set(revealedCellWithBomb)
        cells.notRevealedEmpty shouldBe Set(notRevealedEmptyCell)
        cells.revealedEmpty shouldBe Set(revealedEmptyCell)
        cells.revealedWithBomb shouldBe Set(revealedCellWithBomb)
      }
    }
  }
}
