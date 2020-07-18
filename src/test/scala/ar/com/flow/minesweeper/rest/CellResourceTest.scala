package ar.com.flow.minesweeper.rest

import ar.com.flow.minesweeper.CellValueVisibility.Hidden
import ar.com.flow.minesweeper.{Board, CartesianCoordinates, Cell, Dimensions}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CellResourceTest extends AnyFunSuite with Matchers {
  test("Create CellResources from Cells") {
    val board = Board(Dimensions(2, 2), 2)

    val cellResources = CellResourceFactory.from(board)

    cellResources.foreach(cr => cr.mapsTo(board.getCell(CartesianCoordinates(cr.row, cr.column))) shouldBe true)
  }

  test("Map Cell to CellResource") {
    val cell = new Cell(CartesianCoordinates(1, 1), true, visibility = Hidden)
    val cellResource = CellResource(cell.row, cell.column, cell.hasBomb, cell.visibility)
    cellResource.mapsTo(cell) shouldBe true
  }
}
