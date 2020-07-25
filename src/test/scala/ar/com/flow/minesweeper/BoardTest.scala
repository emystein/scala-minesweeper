package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BoardTest extends AnyFunSuite with Matchers {
  var board = Board(Dimensions(3, 3), 2)

  test("Get cell inside boundaries") {
    board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe None
  }

  test("Trying to get a cell in a row outside the board boundaries should throw an exception") {
    an[NoSuchElementException] should be thrownBy board.cellAt(CartesianCoordinates(board.dimensions.rows + 1, 1))
  }

  test("Trying to get a cell in a column outside the board boundaries should throw an exception") {
    an[NoSuchElementException] should be thrownBy board.cellAt(CartesianCoordinates(1, board.dimensions.columns + 1))
  }

  test("Set cell value") {
    board = board.markCell(CartesianCoordinates(1, 1), Some(CellMark.Flag))

    board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe Some(CellMark.Flag)
  }

  test("Get adjacent cells") {
    val adjacent = board.adjacentCells(board.cellAt(CartesianCoordinates(1, 1)))

    adjacent.map(_.coordinates) shouldBe
      Set(CartesianCoordinates(1, 2), CartesianCoordinates(2, 1), CartesianCoordinates(2, 2))
  }
}
