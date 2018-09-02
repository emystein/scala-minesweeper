package ar.com.flow.minesweeper

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FunSuite, Matchers}

class AdjacentCellsBoardTest extends FunSuite with TableDrivenPropertyChecks with Matchers {
  val data = Table(
    ("cell", "adjacentCoordinates"),
    ((1, 1), Seq((1, 2), (2, 1), (2, 2))),
    ((2, 2), Seq((1, 1), (1, 2), (1, 3), (2, 1), (2, 3), (3, 1), (3, 2), (3, 3))),
    ((3, 3), Seq((2, 2), (2, 3), (3, 2)))
  )

  test("Adjacent cells") {
    forAll(data) { (cell: (Int, Int), adjacentCoordinates: Seq[(Int, Int)]) =>
      val board = BoardFactory(3, 3, 2)
      val adjacentCells = adjacentCoordinates.map(coordinates => board.getCell(coordinates._1, coordinates._2))
      board.adjacentCellsOf(cell._1, cell._2) shouldBe adjacentCells
    }
  }
}
