package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameFinishTest extends AnyFunSuite with Matchers {

  test("Revealing a cell containing a bomb should finish the game as lost") {
    val game = Game(2, 2, 2)

    val bombCell = game.board.cells.withBomb.head

    val updatedGame = game.revealCell(bombCell.coordinates)

    updatedGame.runningState shouldBe GameRunningState.Finished
    updatedGame.result shouldBe Some(GameResult.Lost)
  }

  test("Revealing last empty cell should finish the game as won") {
    val game = Game(2, 2, 2)

    val updatedGame = game.board.cells.empty.foldLeft(game)((game, emptyCell) => game.revealCell(emptyCell.coordinates))

    updatedGame.runningState shouldBe GameRunningState.Finished
    updatedGame.result shouldBe Some(GameResult.Won)
  }
}
