package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameCellStateChangeTest extends AnyWordSpec with Matchers {
  "State of not revealed Cell" when {
    "advance Initial state" should {
      "has Flag mark" in {
        val game = Game(totalRows = 3, totalColumns = 3, totalBombs = 2)

        val updatedGame = game.advanceCellState(CartesianCoordinates(1, 1))

        updatedGame.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe Some(CellMark.Flag)
      }
    }
    "advance Flag state" should {
      "has Question mark" in {
        val game = Game(totalRows = 3, totalColumns = 3, totalBombs = 2)

        val flagCellGame = game.advanceCellState(CartesianCoordinates(1, 1))
        val updatedGame = game.advanceCellState(CartesianCoordinates(1, 1))

        updatedGame.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe Some(CellMark.Question)
      }
    }
    "advance Question state" should {
      "has None mark" in {
        val game = Game(totalRows = 3, totalColumns = 3, totalBombs = 2)

        val flagCellGame = game.advanceCellState(CartesianCoordinates(1, 1))
        val questionCellGame = flagCellGame.advanceCellState(CartesianCoordinates(1, 1))
        val updatedGame = game.advanceCellState(CartesianCoordinates(1, 1))

        updatedGame.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe None
      }
    }
    "Cell is revealed" should {
      "Open Cell" in {
        val game = Game(totalRows = 3, totalColumns = 3, totalBombs = 2)

        val game2 = game.revealCell(CartesianCoordinates(1, 1))

        val game3 = game2.flagCell(CartesianCoordinates(1, 1))
        val game4 = game3.questionCell(CartesianCoordinates(1, 1))

        game4.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe None
      }
    }
  }
}
