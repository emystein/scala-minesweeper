package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.Hidden
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GamePauseResumeTest extends AnyWordSpec with Matchers {
  "Running Game" when {
    "toggle play/pause" should {
      "should pause the game" in {
        val game = Game(3, 3, 2)

        val pausedGame = game.togglePauseResume

        pausedGame.pauseResume shouldBe GamePauseResume.Paused
      }
    }
  }
  "Paused Game" when {
    "toggle play/pause" should {
      "should resume the game" in {
        val game = Game(3, 3, 2)

        val pausedGame = game.togglePauseResume
        val resumedGame = pausedGame.togglePauseResume

        resumedGame.pauseResume shouldBe GamePauseResume.Resumed
      }
    }
    "toggle cell mark" should {
      "ignore the mark" in {
        val game = Game(3, 3, 2)

        val pausedGame = game.togglePauseResume

        val gameWithIgnoredAction = pausedGame.toggleCellMark(CartesianCoordinates(1, 1))

        gameWithIgnoredAction.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe None
      }
    }
    "reveal cell" should {
      "keep cell hidden" in {
        val game = Game(3, 3, 2)

        val pausedGame = game.togglePauseResume

        val gameWithIgnoredAction = pausedGame.revealCell(CartesianCoordinates(1, 1))

        gameWithIgnoredAction.board.cellAt(CartesianCoordinates(1, 1)).visibility shouldBe Hidden
      }
    }
  }
}
