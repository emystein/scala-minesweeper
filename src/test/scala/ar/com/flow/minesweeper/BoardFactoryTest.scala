package ar.com.flow.minesweeper

import org.scalatest.{FunSuite, Matchers}

// TODO: Add tests for checking preconditions on number of rows, columns and bombs
class BoardFactoryTest extends FunSuite with Matchers {
  test("Create board with 3 rows and 3 columns") {
    val board = BoardFactory(3, 3, 2)

    board.totalRows shouldBe 3
    board.totalColumns shouldBe 3
  }

  test("Create board with 2 bombs") {
    val board = BoardFactory(3, 3, 2)

    board.cells.flatten.count(_.hasBomb) shouldBe 2
  }

  test("Adjacent bombs") {
    val board = BoardFactory(3, 3, 2)

    val bombCoordinates = board.cells.flatten.filter(_.hasBomb).map(c => (c.row - 1, c.column - 1))

    for {
      x <- 0 until board.totalRows
      y <- 0 until board.totalColumns
    } yield {
      val adjacentBombs = board.neighboursOf(x, y).count(bombCoordinates.contains)
      board.getCell(x + 1, y + 1).numberOfAdjacentBombs shouldBe adjacentBombs
    }
  }

  test("New board should have all empty cells as remaining") {
    val board = BoardFactory(3, 3, 2)

    board.remainingEmptyCells shouldBe board.emptyCells
  }
}
