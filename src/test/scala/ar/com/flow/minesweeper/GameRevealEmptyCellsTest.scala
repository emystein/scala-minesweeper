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
      implicit val game = Game(rows, columns, bombs)

      val emptyCell = game.board.cells.empty.head

      val adjacentEmptyCells = game.board.adjacentEmptySpace(emptyCell)

      game.revealCell(emptyCell.coordinates)

      allCellsShouldBeRevealed(adjacentEmptyCells)
    })
  }

  test("Revealing bomb cell should not reveal adjacent cells") {
    forAll(data.filter(_._3 > 0))((rows: Int, columns: Int, bombs: Int) => {
      implicit val game = Game(rows, columns, bombs)

      val bombCell = game.board.cells.withBomb.head

      val adjacent = game.board.adjacentCells(bombCell)

      game.revealCell(bombCell.coordinates)

      allCellsShouldBeHidden(adjacent)
    })
  }
}
