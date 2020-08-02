package ar.com.flow.minesweeper

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers


class BoardRevealCellsTest extends AnyFunSpec with TestObjects with Matchers {
  describe("Revealed cells") {
    describe("when revealing a cell") {
      describe("not previously revealed") {
        it("should add cell to revealed cell set") {
          val updatedBoard = board3x3.revealCellAt(coordinatesX1Y1)

          updatedBoard.revealedCells shouldBe Seq(updatedBoard.cellAt(coordinatesX1Y1))
        }
      }
      describe("already revealed") {
        it("should not add cell to revealed cell set") {
          val updatedBoard = board3x3.revealCellAt(coordinatesX1Y1)
          val alreadyRevealedCellBoard = updatedBoard.revealCellAt(coordinatesX1Y1)

          alreadyRevealedCellBoard.revealedCells shouldBe Seq(updatedBoard.cellAt(coordinatesX1Y1))
        }
      }
    }
  }

  describe("Revealed empty cells") {
    describe("when revealed empty cell") {
      it("should contain empty cell") {
        board3x3.revealedCells.filter(_.isEmpty) shouldBe Seq.empty

        val emptyCell = board3x3.emptyCells.head

        val updatedBoard = board3x3.revealCellAt(emptyCell.coordinates)

        val revealedEmptyCell = emptyCell.copy(visibility = Visibility.Shown, board = Some(updatedBoard))

        updatedBoard.revealedCells.filter(_.isEmpty) shouldBe Seq(revealedEmptyCell)
      }
    }
    describe("when revealed bomb cell") {
      it("should not contain bomb cell") {
        board3x3.revealedCells.filter(_.isEmpty) shouldBe Seq.empty

        val bombCell = board3x3.cellsWithBomb.head

        val updatedBoard = board3x3.revealCellAt(bombCell.coordinates)

        val revealedBombCell = bombCell.copy(visibility = Visibility.Shown, board = Some(updatedBoard))

        updatedBoard.revealedCells.filter(_.isEmpty) should not contain revealedBombCell
      }
    }
  }

  describe("Remaining empty cells") {
    describe("when revealed empty cell") {
      it("should be removed from remaining empty cells") {
        val emptyCell = board3x3.emptyCells.head

        val updatedBoard = board3x3.revealCellAt(emptyCell.coordinates)

        updatedBoard.hiddenCells.filter(_.isEmpty) shouldNot contain(emptyCell)
      }
    }
  }
}
