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

    val bombCoordinates = board.cells.flatten.filter(_.hasBomb).map(c => (c.row, c.column))

    for {
      x <- 1 to board.totalRows
      y <- 1 to board.totalColumns
    } yield {
      val adjacentBombs = board.neighboursOf(x, y).count(bombCoordinates.contains)
      board.getCell(x, y).numberOfAdjacentBombs shouldBe adjacentBombs
    }
  }

  test("New board should have all empty cells as remaining") {
    val board = BoardFactory(3, 3, 2)

    board.remainingEmptyCells shouldBe board.emptyCells
  }
}
