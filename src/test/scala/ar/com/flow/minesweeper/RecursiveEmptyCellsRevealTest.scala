package ar.com.flow.minesweeper

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class RecursiveEmptyCellsRevealTest extends FunSuite with TableDrivenPropertyChecks with Matchers with CellsAssertions {
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

      val emptyCell = game.board.emptyCells.head

      val adjacentEmptyCells = game.board.adjacentCellsOf(emptyCell).filter(!_.hasBomb)

      game.revealCell(emptyCell.row, emptyCell.column)

      allCellsShouldBeRevealed(adjacentEmptyCells)
    })
  }

  test("Revealing bomb cell should not reveal adjacent cells") {
    forAll(data.filter(_._3 > 0))((rows: Int, columns: Int, bombs: Int) => {
      implicit val game = Game(rows, columns, bombs)

      val bombCell = game.board.bombCells.head

      val adjacent = game.board.adjacentCellsOf(bombCell)

      game.revealCell(bombCell.row, bombCell.column)

      allCellsShouldBeHidden(adjacent)
    })
  }


}
