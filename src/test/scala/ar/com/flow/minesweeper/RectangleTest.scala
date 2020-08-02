package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class RectangleTest extends AnyFunSuite with TableDrivenPropertyChecks with Matchers {
  val data = Table(
    ("rows", "columns", "row", "column", "adjacent"),
    (3, 3, 1, 1, Set((1, 2), (2, 1), (2, 2))),
    (3, 3, 2, 2, Set((1, 1), (1, 2), (1, 3), (2, 1), (2, 3), (3, 1), (3, 2), (3, 3))),
    (3, 3, 3, 3, Set((2, 2), (2, 3), (3, 2)))
  )

  test("Adjacent cells") {
    forAll(data)((rows: Int, columns: Int, row: Int, column: Int, adjacent: Set[(Int, Int)]) => {
      val rectangle = new RectangleCoordinates {
        val dimensions = Dimensions(rows, columns)
      }

      val allAdjacent = adjacent.map(n => CartesianCoordinates(n._1, n._2))

      rectangle.adjacentOf(CartesianCoordinates(row, column)) shouldBe allAdjacent
    })
  }
}
