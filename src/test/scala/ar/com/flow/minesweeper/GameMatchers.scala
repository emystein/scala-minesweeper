package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.Hidden
import org.scalatest._
import matchers._

trait GameMatchers {
  class CellHiddenMatcher(coordinates: CartesianCoordinates) extends Matcher[Game] {
    def apply(game: Game) = {
      MatchResult(
        game.board.cellAt(coordinates).visibility == Hidden,
        s"""Game doesn't have hidden cell at $coordinates""",
        s"""Game has hidden cell at $coordinates"""
      )
    }
  }

  def haveCellHiddenAt(coordinates: CartesianCoordinates) = new CellHiddenMatcher(coordinates)

  class GameRunningStateMatcher(expectedState: GameRunningState) extends Matcher[Game] {
    def apply(game: Game) = {
      MatchResult(
        game.runningState == expectedState,
        s"""Game is not $expectedState"""",
        s"""Game is $expectedState""""
      )
    }
  }

  class GameFinishMatcher(expectedResult: GameResult) extends Matcher[Game] {
    def apply(game: Game) = {
      MatchResult(
        game.runningState == GameRunningState.Finished && game.result.contains(expectedResult),
        s"""Game result is not $expectedResult"""",
        s"""Game result is $expectedResult""""
      )
    }
  }

  def beRunning() = new GameRunningStateMatcher(GameRunningState.Running)
  def bePaused() = new GameRunningStateMatcher(GameRunningState.Paused)
  def finished() = new GameRunningStateMatcher(GameRunningState.Finished)
  def beWon() = new GameFinishMatcher(GameResult.Won)
  def beLost() = new GameFinishMatcher(GameResult.Lost)
}

// Make them easy to import with:
// import GameMatchers._
object GameMatchers extends GameMatchers