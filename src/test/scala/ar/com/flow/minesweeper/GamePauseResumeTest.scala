package ar.com.flow.minesweeper

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class GamePauseResumeTest extends AnyFunSpec with Matchers {
  describe("New Game") {
    describe("when pausing the game") {
      it("should pause the game") {
        val game = Game(3, 3, 2)

        game.pause.state shouldBe GameState(GamePlayStatus.Paused, GameResult.Pending)
      }
    }
    describe("when resuming the game") {
      it("should resume the game") {
        val game = Game(3, 3, 2)

        game.resume.state shouldBe GameState(GamePlayStatus.Playing, GameResult.Pending)
      }
    }
  }

  describe("GamePlayStatus.Paused Game") {
    describe("when pausing the game") {
      it("should pause the game") {
        val game = Game(3, 3, 2)

        game.pause.pause.state shouldBe GameState(GamePlayStatus.Paused, GameResult.Pending)
      }
    }
    describe("when resuming the game") {
      it("should resume the game") {
        val game = Game(3, 3, 2)

        game.pause.resume.state shouldBe GameState(GamePlayStatus.Playing, GameResult.Pending)
      }
    }
  }
}
