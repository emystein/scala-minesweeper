package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.CellsMatchers._
import ar.com.flow.minesweeper.GameMatchers._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class GameRevealEmptyCellsTest extends AnyFunSuite with TableDrivenPropertyChecks with TestObjects with Matchers {
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
      val game = Game(rows, columns, bombs)

      val emptyCell = game.board.emptyCells.head

      val updatedGame = game.revealCell(emptyCell.coordinates)

      updatedGame.board.cellAt(emptyCell.coordinates).adjacentEmptySpace() should allBeRevealed
    })
  }

  test("Revealing bomb cell should not reveal adjacent cells") {
    forAll(data.filter(_._3 > 0))((rows: Int, columns: Int, bombs: Int) => {
      val game = Game(rows, columns, bombs)

      val bombCell = game.board.cellsWithBomb.head

      val updatedGame = game.revealCell(bombCell.coordinates)

      updatedGame.board.cellAt(bombCell.coordinates).adjacentCells should allBeHidden
    })
  }

  test("Revealing an empty cell and having remaining empty cells should keep the game playing") {
    val emptyCell = game.board.emptyCells.head

    val updatedGame = game.revealCell(emptyCell.coordinates)

    if (updatedGame.board.hiddenCells.empty.isEmpty) {
      updatedGame should beWon
    } else {
      updatedGame should beRunning
      updatedGame.result shouldBe None
    }
  }
}
