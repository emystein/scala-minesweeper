package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.CellsMatchers._
import ar.com.flow.minesweeper.Visibility.Hidden
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

// TODO: Add precondition for board with at least 1 more empty cell than bombs
class GameTest extends AnyFunSuite with TestObjects with Matchers {
  test("New Game should have all Cells hidden") {
    game.board.allCells.foreach(cell => cell.visibility shouldBe Hidden)
  }

  test("Toggle Cell mark") {
    val updatedGame = game.toggleCellMark(coordinatesX1Y1)

    updatedGame.board.cellAt(coordinatesX1Y1) should beMarked
  }

  test("Reveal Cell should mark it as revealed") {
    val updatedGame = game.revealCell(coordinatesX1Y1)

    val cell = updatedGame.board.cellAt(coordinatesX1Y1)

    cell should beRevealed
    cell shouldNot beMarked
  }
}
