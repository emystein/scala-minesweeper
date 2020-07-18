package ar.com.flow.minesweeper.rest

import ar.com.flow.minesweeper.CellContent.Bomb
import ar.com.flow.minesweeper.Visibility.Hidden
import ar.com.flow.minesweeper.{Board, CartesianCoordinates, Cell, Dimensions}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CellResourceTest extends AnyFunSuite with Matchers with CellResourceAssertions {
  test("Create CellResources from Cells") {
    val board = Board(Dimensions(2, 2), 2)

    CellResources.from(board)
      .foreach(cr => same(cr, board.cellAt(cr.coordinates)) shouldBe true)
  }

  test("Map Cell to CellResource") {
    val cell = new Cell(CartesianCoordinates(1, 1), Some(Bomb), visibility = Hidden)
    val cellResource = CellResource(cell.coordinates, cell.content, cell.visibility)
    same(cellResource, cell) shouldBe true
  }
}

trait CellResourceAssertions {
  def same(resource: CellResource, cell: Cell): Boolean = {
    resource.coordinates == cell.coordinates &&
      resource.content == cell.content &&
      resource.visibility == cell.visibility &&
      resource.mark == cell.mark
  }
}

