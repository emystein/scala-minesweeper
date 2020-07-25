package ar.com.flow.minesweeper

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class GamePauseResumeTest extends AnyFunSpec with Matchers {
  describe("New Game") {
    describe("when pausing the game") {
      it("should pause the game") {
        val game = Game(3, 3, 2)

        game.pause.playStatus shouldBe GamePlayStatus.Paused
      }
    }
    describe("when resuming the game") {
      it("should resume the game") {
        val game = Game(3, 3, 2)

        game.resume.playStatus shouldBe GamePlayStatus.Playing
      }
    }
  }

  describe("GamePlayStatus.Paused Game") {
    describe("when pausing the game") {
      it("should pause the game") {
        val game = Game(3, 3, 2)

        game.pause.pause.playStatus shouldBe GamePlayStatus.Paused
      }
    }
    describe("when resuming the game") {
      it("should resume the game") {
        val game = Game(3, 3, 2)

        game.pause.resume.playStatus shouldBe GamePlayStatus.Playing
      }
    }
  }
}
