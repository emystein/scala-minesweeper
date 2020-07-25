package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

// TODO: Add precondition for board with at least 1 more empty cell than bombs
class GameTest extends AnyFunSuite with Matchers {
  test("New Game should have all Cells hidden") {
    val game = Game(totalRows = 3, totalColumns = 3, totalBombs = 2)

    game.board.cells.all.foreach(cell => cell.visibility shouldBe Hidden)
  }

  test("Question Cell") {
    val game = Game(totalRows = 3, totalColumns = 3, totalBombs = 2)

    val updatedGame = game.questionCell(CartesianCoordinates(1, 1))

    updatedGame.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe Some(CellMark.Question)
  }

  test("Flag Cell") {
    val game = Game(totalRows = 3, totalColumns = 3, totalBombs = 2)

    val updatedGame = game.flagCell(CartesianCoordinates(1, 1))

    updatedGame.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe Some(CellMark.Flag)
  }

  test("Reveal Cell should mark it as revealed") {
    val game = Game(totalRows = 3, totalColumns = 3, totalBombs = 2)

    val updatedGame = game.revealCell(CartesianCoordinates(1, 1))

    updatedGame.board.cellAt(CartesianCoordinates(1, 1)).visibility shouldBe Shown
    updatedGame.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe None
  }
}
