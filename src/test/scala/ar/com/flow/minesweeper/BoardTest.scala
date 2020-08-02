package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BoardTest extends AnyFunSuite with TestObjects with Matchers {
  var board = Board(Dimensions(3, 3), totalBombs = 2)

  test("Create board with 3 rows and 3 columns and 2 bombs") {
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

  test("Get cell inside boundaries") {
    board.cellAt(coordinatesX1Y1).mark shouldBe None
  }

  test("Trying to get a cell in a row outside the board boundaries should throw an exception") {
    an[NoSuchElementException] should be thrownBy board.cellAt(CartesianCoordinates(board.dimensions.rows + 1, 1))
  }

  test("Trying to get a cell in a column outside the board boundaries should throw an exception") {
    an[NoSuchElementException] should be thrownBy board.cellAt(CartesianCoordinates(1, board.dimensions.columns + 1))
  }

  test("Get adjacent cells") {
    val adjacent = board.cellAt(coordinatesX1Y1).adjacentCells

    adjacent.map(_.coordinates) shouldBe Set(coordinatesX1Y2, coordinatesX2Y1, coordinatesX2Y2)
  }

  test("Advance Cell Mark") {
    board = board.toggleMarkAt(coordinatesX1Y1)

    board.cellAt(coordinatesX1Y1).mark shouldBe Some(CellMark.Flag)
  }
}
