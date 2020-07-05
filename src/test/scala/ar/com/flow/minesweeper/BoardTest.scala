package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BoardTest extends AnyFunSuite with Matchers {
  val board = Board(Dimensions(3, 3), 2)

  test("Get cell inside boundaries") {
    board.getCell(1, 1).value shouldBe CellValue.empty
  }

  test("Trying to get a cell in a row outside the board boundaries should throw an exception") {
    an[NoSuchElementException] should be thrownBy board.getCell(board.totalRows + 1, 1)
  }

  test("Trying to get a cell in a column outside the board boundaries should throw an exception") {
    an[NoSuchElementException] should be thrownBy board.getCell(1, board.totalColumns + 1)
  }

  test("Set cell value") {
    board.setCellValue((1, 1), CellValue.flag)

    board.getCell(1, 1).value shouldBe CellValue.flag
  }
}
