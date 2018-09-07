package ar.com.flow.minesweeper

import org.scalatest.{FunSuite, Matchers}

// TODO: Add precondition for board with at least 1 more empty cell than bombs
class GameTest extends FunSuite with Matchers {
  test("Question Cell") {
    val game = GameFactory.createGame(3, 3, 2)

    game.questionCell(1, 1)

    game.board.getCell(1, 1).value shouldBe CellValue.question
  }

  test("Flag Cell") {
    val game = GameFactory.createGame(3, 3, 2)

    game.flagCell(1, 1)

    game.board.getCell(1, 1).value shouldBe CellValue.flag
  }

  test("Reveal Cell should mark it as revealed") {
    val game = GameFactory.createGame(3, 3, 2)

    game.board.getCell(1, 1).isRevealed shouldBe false

    game.revealCell(1, 1)

    game.board.getCell(1, 1).isRevealed shouldBe true
  }

  test("Revealing an empty cell and having remaining empty cells should keep the game playing") {
    val game = GameFactory.createGame(3, 3, 2)

    val emptyCell = game.board.emptyCells.head

    game.revealCell(emptyCell.row, emptyCell.column)

    if (game.board.remainingEmptyCells.isEmpty) {
      // if recursive cell reveal won the game
      game.state shouldBe GameState.finished
      game.result shouldBe GameResult.won
    } else {
      game.state shouldBe  GameState.playing
      game.result shouldBe GameResult.pending
    }
  }

  test("Revealing a cell containing a bomb should finish the game as lost") {
    val game = GameFactory.createGame(3, 3, 2)

    val bombCell = game.board.bombCells.head

    game.revealCell(bombCell.row, bombCell.column)

    game.state shouldBe GameState.finished
    game.result shouldBe GameResult.lost
  }

  test("Revealing last empty cell should finish the game as won") {
    val game = GameFactory.createGame(3, 3, 2)

    game.board.emptyCells.foreach(emptyCell => game.revealCell(emptyCell.row, emptyCell.column))

    game.state shouldBe GameState.finished
    game.result shouldBe GameResult.won
  }
}
