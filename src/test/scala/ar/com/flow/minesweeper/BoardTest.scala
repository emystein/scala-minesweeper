package ar.com.flow.minesweeper

import org.scalatest.{FunSuite, Matchers}

class BoardTest extends FunSuite with Matchers {
  val board = new Board(3, 3, 2)

  test("Get cell inside boundaries") {
    board.getCell(1, 1).value shouldBe CellValue.NONE
  }

  test("Trying to get a cell in a row outside the board boundaries should throw an exception") {
    an [IndexOutOfBoundsException] should be thrownBy board.getCell(board.totalRows + 1, 1)
  }

  test("Trying to get a cell in a column outside the board boundaries should throw an exception") {
    an [IndexOutOfBoundsException] should be thrownBy board.getCell(1, board.totalColumns + 1)
  }

  test("Set cell value") {
    board.setCellValue(1, 1, CellValue.FLAG)

    board.getCell(1, 1).value shouldBe CellValue.FLAG
  }

  test("Empty cells should be equal to all cells minus bomb cells") {
    val board = new Board(3, 3, 2)

    board.emptyCells shouldBe board.cells.flatten.toSet -- board.bombCells
  }


}
