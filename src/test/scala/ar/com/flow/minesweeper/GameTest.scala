package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

// TODO: Add precondition for board with at least 1 more empty cell than bombs
class GameTest extends AnyFunSuite with Matchers {
  test("Question Cell") {
    val game = Game(3, 3, 2)

    game.questionCell(CartesianCoordinates(1, 1))

    game.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe Some(CellMark.Question)
  }

  test("Flag Cell") {
    val game = Game(3, 3, 2)

    game.flagCell(CartesianCoordinates(1, 1))

    game.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe Some(CellMark.Flag)
  }

  test("Reveal Cell should mark it as revealed") {
    val game = Game(3, 3, 2)

    game.board.cellAt(CartesianCoordinates(1, 1)).visibility shouldBe Hidden

    game.revealCell(CartesianCoordinates(1, 1))

    game.board.cellAt(CartesianCoordinates(1, 1)).visibility shouldBe Shown
  }
}
