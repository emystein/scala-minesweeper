package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameStateTest extends AnyFunSuite with Matchers {
  test("Revealing an empty cell and having remaining empty cells should keep the game playing") {
    val game = Game(3, 3, 2)

    val emptyCell = game.board.cells.empty.head

    game.revealCell(emptyCell.row, emptyCell.column)

    if (game.board.cells.remainingEmpty.isEmpty) {
      // if recursive cell reveal won the game
      game.state shouldBe GameState(GamePlayStatus.finished, GameResult.won)
    } else {
      game.state shouldBe GameState(GamePlayStatus.playing, GameResult.pending)
    }
  }

  test("Revealing a cell containing a bomb should finish the game as lost") {
    val game = Game(3, 3, 2)

    val bombCell = game.board.cells.withBomb.head

    game.revealCell(bombCell.row, bombCell.column)

    game.state shouldBe GameState(GamePlayStatus.finished, GameResult.lost)
  }

  test("Revealing last empty cell should finish the game as won") {
    val game = Game(3, 3, 2)

    game.board.cells.empty.foreach(emptyCell => game.revealCell(emptyCell.row, emptyCell.column))

    game.state shouldBe GameState(GamePlayStatus.finished, GameResult.won)
  }

}
