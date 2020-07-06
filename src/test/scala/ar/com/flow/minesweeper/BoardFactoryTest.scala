package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BoardFactoryTest extends AnyFunSuite with Matchers {
  test("Create board with 3 rows and 3 columns and 2 bombs") {
    val board = Board(Dimensions(3, 3), 2)

    board.dimensions shouldBe Dimensions(3, 3)
    board.cellsByCoordinates should have size 9
    board.cells.withBomb should have size 2
  }

  test("New board should have all empty cells as remaining") {
    val board = Board(Dimensions(3, 3), 2)

    board.cells.notRevealedEmpty shouldBe board.cells.empty
  }

  test("New board should not have negative bombs") {
    assertThrows[IllegalArgumentException] {
      Board(Dimensions(3, 3), -1)
    }
  }
}
