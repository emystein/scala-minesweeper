package ar.com.flow.minesweeper

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks

class BoardAdjacentCellsTest extends FunSuite with TableDrivenPropertyChecks with Matchers {
  val data = Table(
    ("rows", "columns", "row", "column", "adjacentCoordinates"),
    (3, 3, 1, 1, Seq((1, 2), (2, 1), (2, 2))),
    (3, 3, 2, 2, Seq((1, 1), (1, 2), (1, 3), (2, 1), (2, 3), (3, 1), (3, 2), (3, 3))),
    (3, 3, 3, 3, Seq((2, 2), (2, 3), (3, 2)))
  )

  test("Adjacent cells") {
    forAll(data)((rows: Int, columns: Int, row: Int, column: Int, adjacentCoordinates: Seq[(Int, Int)]) => {
      val board = BoardFactory(rows, columns, 0)
      board.adjacentCellsOf(row, column) shouldBe adjacentCoordinates.map(c => board.getCell(c._1, c._2))
    })
  }
}
