package ar.com.flow.minesweeper

import org.scalatest.{FunSuite, Matchers}

// TODO: Add tests for checking preconditions on number of rows, columns and bombs
class BoardFactoryTest extends FunSuite with Matchers {
  test("Create board with 3 rows and 3 columns") {
    val board = BoardFactory(3, 3, 2)

    board.totalRows shouldBe 3
    board.totalColumns shouldBe 3
    board.cells.size shouldBe 9
  }

  test("Create board with 2 bombs") {
    val board = BoardFactory(3, 3, 2)

    board.bombCells.size shouldBe 2
  }

  test("Adjacent bombs") {
    val board = BoardFactory(3, 3, 2)
    val cellLocationContext = new CellLocationContext(board)

    for {
      cell <- board.cells
    } yield {
      val adjacentBombs = cellLocationContext.neighboursOf(cell)
        .map(board.getCell)
        .count(board.bombCells.contains)

      cell.numberOfAdjacentBombs shouldBe adjacentBombs
    }
  }

  test("New board should have all empty cells as remaining") {
    val board = BoardFactory(3, 3, 2)

    board.remainingEmptyCells shouldBe board.emptyCells
  }
}
