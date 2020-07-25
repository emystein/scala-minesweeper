package ar.com.flow.minesweeper

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class GamePauseResumeTest extends AnyFunSpec with Matchers {
  describe("Running Game") {
    describe("when toggling play/pause") {
      it("should pause the game") {
        val game = Game(3, 3, 2)

        val pausedGame = game.togglePauseResume

        pausedGame.playStatus shouldBe GamePlayStatus.Paused
      }
    }
  }
  describe("Paused Game") {
    describe("when toggling play/pause") {
      it("should resume the game") {
        val game = Game(3, 3, 2)

        val pausedGame = game.togglePauseResume
        val resumedGame = pausedGame.togglePauseResume

        resumedGame.playStatus shouldBe GamePlayStatus.Playing
      }
    }
  }
}
