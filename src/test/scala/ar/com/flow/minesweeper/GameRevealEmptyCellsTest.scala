package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class GameRevealEmptyCellsTest extends AnyFunSuite with TableDrivenPropertyChecks with Matchers with CellsAssertions {
  val data = Table(
    ("rows", "columns", "bombs"),
    (1, 1, 0),
    (2, 2, 0),
    (2, 2, 1),
    (3, 3, 0),
    (3, 3, 1),
    (3, 3, 2)
  )

  test("Revealing empty cell with adjacent empty cells should reveal the adjacent empty cells as well") {
    forAll(data)((rows: Int, columns: Int, bombs: Int) => {
      implicit var game = Game(rows, columns, bombs)

      val emptyCell = game.board.cells.empty.head

      val adjacentEmptyCells = emptyCell.adjacentEmptySpace()

      game = game.revealCell(emptyCell.coordinates)

      allCellsShouldBeRevealed(adjacentEmptyCells)
    })
  }

  test("Revealing bomb cell should not reveal adjacent cells") {
    forAll(data.filter(_._3 > 0))((rows: Int, columns: Int, bombs: Int) => {
      implicit var game = Game(rows, columns, bombs)

      val bombCell = game.board.cells.withBomb.head

      val adjacent = bombCell.adjacentCells

      game = game.revealCell(bombCell.coordinates)

      allCellsShouldBeHidden(adjacent)
    })
  }

  test("Revealing an empty cell and having remaining empty cells should keep the game playing") {
    val game = Game(2, 2, 2)

    val emptyCell = game.board.cells.empty.head

    val updatedGame = game.revealCell(emptyCell.coordinates)

    if (updatedGame.board.cells.hidden.empty.isEmpty) {
      // if recursive cell reveal won the game
      updatedGame.runningState shouldBe GameRunningState.Finished
      updatedGame.result shouldBe Some(GameResult.Won)
    } else {
      updatedGame.runningState shouldBe GameRunningState.Running
      updatedGame.result shouldBe None
    }
  }
}
