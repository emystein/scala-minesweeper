package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import GameMatchers._

class GameFinishTest extends AnyWordSpec with TestObjects with Matchers {
  "Running Game" when {
    "Reveal a Cell containing a Bomb" should {
      "Loose the Game" in {
        val bombCell = game.board.cellsWithBomb.head

        val updatedGame = game.revealCell(bombCell.coordinates)

        updatedGame should beLost
      }
    }
    "Reveal all empty Cells" should {
      "Win the Game" in {
        val updatedGame = game.board.emptyCells.foldLeft(game) {
          (game, emptyCell) => game.revealCell(emptyCell.coordinates)
        }

        updatedGame should beWon
      }
    }
  }
}
