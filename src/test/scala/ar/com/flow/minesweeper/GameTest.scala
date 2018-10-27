package ar.com.flow.minesweeper

import org.scalatest.{FunSuite, Matchers}

// TODO: Add precondition for board with at least 1 more empty cell than bombs
class GameTest extends FunSuite with Matchers {
  test("Question Cell") {
    val game = Game(3, 3, 2)

    game.questionCell((1, 1))

    game.board.getCell((1, 1)).value shouldBe CellValue.question
  }

  test("Flag Cell") {
    val game = Game(3, 3, 2)

    game.flagCell((1, 1))

    game.board.getCell((1, 1)).value shouldBe CellValue.flag
  }

  test("Reveal Cell should mark it as revealed") {
    val game = Game(3, 3, 2)

    game.board.getCell((1, 1)).isRevealed shouldBe false

    game.revealCell((1, 1))

    game.board.getCell((1, 1)).isRevealed shouldBe true
  }
}
