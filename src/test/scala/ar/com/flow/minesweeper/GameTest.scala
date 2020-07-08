package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

// TODO: Add precondition for board with at least 1 more empty cell than bombs
class GameTest extends AnyFunSuite with Matchers {
  test("Question Cell") {
    val game = Game(3, 3, 2)

    game.questionCell(CartesianCoordinates(1, 1))

    game.board.getCell((1, 1)).value shouldBe CellValue.question
  }

  test("Flag Cell") {
    val game = Game(3, 3, 2)

    game.flagCell(CartesianCoordinates(1, 1))

    game.board.getCell((1, 1)).value shouldBe CellValue.flag
  }

  test("Reveal Cell should mark it as revealed") {
    val game = Game(3, 3, 2)

    game.board.getCell((1, 1)).isRevealed shouldBe false

    game.revealCell(CartesianCoordinates(1, 1))

    game.board.getCell((1, 1)).isRevealed shouldBe true
  }
}
