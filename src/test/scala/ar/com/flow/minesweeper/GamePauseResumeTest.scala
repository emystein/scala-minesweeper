package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.Hidden
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import GameMatchers._

class GamePauseResumeTest extends AnyWordSpec with TestObjects with Matchers {
  "Running Game" when {
    "toggle play/pause" should {
      "pause the Game" in {
        val pausedGame = game.togglePauseResume

        pausedGame should bePaused
      }
    }
  }
  "Paused Game" when {
    val pausedGame = game.togglePauseResume

    "toggle play/pause" should {
      "resume the Game" in {
        val resumedGame = pausedGame.togglePauseResume

        resumedGame should beRunning
      }
    }
    "toggle Cell mark" should {
      "ignore the mark" in {
        pausedGame.toggleCellMark(coordinatesX1Y1) shouldBe theSameInstanceAs(pausedGame)
      }
    }
    "reveal Cell" should {
      "keep Cell hidden" in {
        val gameWithIgnoredAction = pausedGame.revealCell(coordinatesX1Y1)

        gameWithIgnoredAction should haveCellHiddenAt(coordinatesX1Y1)
      }
    }
  }
}
