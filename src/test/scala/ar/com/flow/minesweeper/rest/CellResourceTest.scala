package ar.com.flow.minesweeper.rest

import ar.com.flow.minesweeper.{Board, Cell, Dimensions}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CellResourceTest extends AnyFunSuite with Matchers {
  test("Create CellResources from Cells") {
    val board = Board(Dimensions(2, 2), 2)

    val cellResources = CellResourceFactory.from(board)

    cellResources.foreach(cr => cr.mapsTo(board.getCell(cr.row, cr.column)) shouldBe true)
  }

  test("Map Cell to CellResource") {
    val cell = new Cell(1, 1, true, false)
    val cellResource = CellResource(cell.row, cell.column, cell.hasBomb, cell.isRevealed)
    cellResource.mapsTo(cell) shouldBe true
  }
}
