package ar.com.flow.minesweeper

import org.scalatest.{FunSuite, Matchers}

class CellResourceTest extends FunSuite with Matchers {
  test("Create CellResources from Cells") {
    val board = Board(2, 2, 2)

    val cellResources = CellResourceFactory.from(board.cells)

    cellResources.foreach(cr => cr.mapsTo(board.getCell(cr.row, cr.column)) shouldBe true)
  }

  test("Map Cell to CellResource") {
    val cell = new Cell(1, 1, true, 0, false)
    val cellResource = CellResource(cell.row, cell.column, cell.hasBomb, cell.numberOfAdjacentBombs, cell.isRevealed)
    cellResource.mapsTo(cell) shouldBe true
  }
}
