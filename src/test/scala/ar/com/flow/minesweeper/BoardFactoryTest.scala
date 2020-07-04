package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BoardFactoryTest extends AnyFunSuite with Matchers {
  test("Create board with 3 rows and 3 columns and 2 bombs") {
    val board = Board(3, 3, 2)

    board.totalRows shouldBe 3
    board.totalColumns shouldBe 3
    board.cellsByCoordinates should have size 9
    board.cells.withBomb should have size 2
  }

  test("Adjacent bombs") {
    val board = Board(3, 3, 2)

    for {
      cell <- board.cells
    } yield {
      val adjacentBombs = board.adjacentCellsOf(cell).count(board.cells.withBomb.contains)

      cell.numberOfAdjacentBombs shouldBe adjacentBombs
    }
  }

  test("New board should have all empty cells as remaining") {
    val board = Board(3, 3, 2)

    board.cells.remainingEmpty shouldBe board.cells.empty
  }

  test("New board should have more than 0 rows") {
    Range.inclusive(-1, 0).foreach(row =>
      assertThrows[IllegalArgumentException] {
        Board(row, 3, 2)
      }
    )
  }

  test("New board should have more than 0 columns") {
    Range.inclusive(-1, 0).foreach(column =>
      assertThrows[IllegalArgumentException] {
        Board(3, column, 2)
      }
    )
  }

  test("New board should not have negative bombs") {
    assertThrows[IllegalArgumentException] {
      Board(3, 3, -1)
    }
  }
}
