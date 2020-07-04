package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class CellLocationContextTest extends AnyFunSuite with TableDrivenPropertyChecks with Matchers {
  val data = Table(
    ("rows", "columns", "row", "column", "neighbours"),
    (3, 3, 1, 1, Seq((1, 2), (2, 1), (2, 2))),
    (3, 3, 2, 2, Seq((1, 1), (1, 2), (1, 3), (2, 1), (2, 3), (3, 1), (3, 2), (3, 3))),
    (3, 3, 3, 3, Seq((2, 2), (2, 3), (3, 2)))
  )

  test("Cell neighbours") {
    forAll(data)((rows: Int, columns: Int, row: Int, column: Int, neighbours: Seq[Board.Coordinates]) => {
      val cellLocationContext = new CellLocationContext(rows, columns)
      cellLocationContext.neighboursOf(row, column) shouldBe neighbours
    })
  }
}
