package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.GamePlayStatus.{paused, playing}
import org.scalatest.{FunSpec, Matchers}

class GamePauseResumeTest extends FunSpec with Matchers {
  describe("New Game") {
    describe("when pausing the game") {
      it("should pause the game") {
        val game = GameFactory.createGame(3, 3, 2)

        game.pause

        game.state shouldBe GameState(paused, GameResult.pending)
      }
    }
    describe("when resuming the game") {
      it("should resume the game") {
        val game = GameFactory.createGame(3, 3, 2)

        game.resume

        game.state shouldBe GameState(playing, GameResult.pending)
      }
    }
  }

  describe("Paused Game") {
    describe("when pausing the game") {
      it("should pause the game") {
        val game = GameFactory.createGame(3, 3, 2)

        game.pause
        game.pause

        game.state shouldBe GameState(paused, GameResult.pending)
      }
    }
    describe("when resuming the game") {
      it("should resume the game") {
        val game = GameFactory.createGame(3, 3, 2)

        game.pause
        game.resume

        game.state shouldBe GameState(playing, GameResult.pending)
      }
    }
  }
}
