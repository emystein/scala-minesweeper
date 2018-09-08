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
      val cellLocationContext = new CellLocationContext(rows, columns)

      val emptyCell = game.board.emptyCells.head

      val adjacentEmptyCells = cellLocationContext.neighboursOf(emptyCell.row, emptyCell.column)
        .map(game.board.getCell)
        .filter(!_.hasBomb)

      game.revealCell(emptyCell.row, emptyCell.column)

      adjacentEmptyCells.foreach(cell => game.board.getCell(cell.row, cell.column).isRevealed shouldBe true)
    })
  }

  test("Revealing bomb cell should not reveal adjacent cells") {
    forAll(data.filter(_._3 > 0))((rows: Int, columns: Int, bombs: Int) => {
      val game = GameFactory.createGame(rows, columns, bombs)
      val cellLocationContext = new CellLocationContext(rows, columns)

      val bombCell = game.board.bombCells.head

      val adjacent = cellLocationContext.neighboursOf(bombCell.row, bombCell.column)
        .map(game.board.getCell)

      game.revealCell(bombCell.row, bombCell.column)

      adjacent.foreach(cell => game.board.getCell(cell.row, cell.column).isRevealed shouldBe false)
    })
  }
}
