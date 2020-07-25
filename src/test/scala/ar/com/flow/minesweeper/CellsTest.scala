package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellsTest extends AnyWordSpec with TestCells with Matchers {
  "Cells" when {
    "created" should {
      "discriminate empty, bombs, revealed cells" in {
        val board = Board(Dimensions(3, 3), 2)

        val cells = Cells(board.cellsByCoordinates.values)

        cells.all.size shouldBe 9
        cells.empty shouldBe cells.all.filter(_.content == CellContent.Empty)
        cells.withBomb shouldBe cells.all.filter(_.content == CellContent.Bomb)
        cells.hidden shouldBe CellContentFilters(cells.all.filter(_.visibility == Visibility.Hidden))
        cells.revealed shouldBe CellContentFilters(cells.all.filter(_.visibility == Visibility.Shown))
      }
    }
  }
}
