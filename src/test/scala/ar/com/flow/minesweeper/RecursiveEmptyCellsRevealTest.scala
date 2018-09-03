package ar.com.flow.minesweeper

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class RecursiveEmptyCellsRevealTest extends FunSuite with TableDrivenPropertyChecks with Matchers  {
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
      val game = GameFactory.createGame(rows, columns, bombs)

      val emptyCell = game.board.emptyCells.head

      val adjacentEmptyCells = game.board.adjacentCellsOf(emptyCell.row, emptyCell.column).filter(cell => !cell.hasBomb)

      game.revealCell(emptyCell.row, emptyCell.column)

      adjacentEmptyCells.foreach(cell => game.board.getCell(cell.row, cell.column).isRevealed shouldBe true)
    })
  }
}
