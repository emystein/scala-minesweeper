package ar.com.flow.minesweeper.rest

import ar.com.flow.minesweeper.Visibility.Hidden
import ar.com.flow.minesweeper._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CellResourceTest extends AnyFunSuite with Matchers with CellResourceAssertions {
  val board: Board = Board(Dimensions(3, 3), totalBombs = 3)

  test("Create CellResources from Cells") {
    val board = Board(Dimensions(2, 2), 2)

    CellResources.from(board)
      .foreach(cr => same(cr, board.cellAt(cr.coordinates)) shouldBe true)
  }

  test("Map Cell to CellResource") {
    val cell = new Cell(CartesianCoordinates(1, 1), CellContent.Bomb, visibility = Hidden, mark = None)
    val cellResource = CellResource.from(cell)
    same(cellResource, cell) shouldBe true
  }
}

trait CellResourceAssertions {
  def same(resource: CellResource, cell: Cell): Boolean = {
    resource.coordinates == cell.coordinates &&
      resource.hasBomb == (cell.content == CellContent.Bomb) &&
      resource.hasVisibility(cell.visibility) &&
      resource.mark == cell.mark
  }
}

