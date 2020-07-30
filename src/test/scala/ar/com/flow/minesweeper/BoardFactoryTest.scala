package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BoardFactoryTest extends AnyFunSuite with Matchers {
  test("Create board with 3 rows and 3 columns and 2 bombs") {
    val board = Board(Dimensions(3, 3), totalBombs = 2)

    board.dimensions shouldBe Dimensions(3, 3)
    board.contentByCoordinates should have size 9
    board.cellsWithBomb should have size 2
    board.hiddenCells.filter(_.isEmpty) shouldBe board.emptyCells
    board.revealedCells shouldBe Seq.empty
  }

  test("New board should not have negative bombs") {
    assertThrows[IllegalArgumentException] {
      Board(Dimensions(3, 3), totalBombs = -1)
    }
  }
}
