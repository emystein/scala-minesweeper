package ar.com.flow.minesweeper

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class RevealedCellsBoardTest extends AnyFunSpec with Matchers {
  describe("Revealed cells") {
    describe("when creating a Board") {
      it("should be an empty set") {
        val board = Board(Dimensions(3, 3), 2)

        board.cells.revealed.all shouldBe Set.empty
      }
    }
    describe("when revealing a cell") {
      describe("not previously revealed") {
        it("should add cell to revealed cell set") {
          val board = Board(Dimensions(3, 3), 2)

          val updatedBoard = board.revealCell(CartesianCoordinates(1, 1))

          updatedBoard.cells.revealed.all shouldBe Set(updatedBoard.cellAt(CartesianCoordinates(1, 1)))
        }
      }
      describe("already revealed") {
        it("should not add cell to revealed cell set") {
          val board = Board(Dimensions(3, 3), 2)

          val updatedBoard = board.revealCell(CartesianCoordinates(1, 1))

          updatedBoard.cells.revealed.all shouldBe Set(updatedBoard.cellAt(CartesianCoordinates(1, 1)))
        }
      }
    }
  }

  describe("Revealed empty cells") {
    describe("when revealed empty cell") {
      it("should contain empty cell") {
        val board = Board(Dimensions(3, 3), 2)

        board.cells.revealed.empty shouldBe Set()

        val emptyCell = board.cells.empty.head

        val updatedBoard = board.revealCell(emptyCell.coordinates)

        val revealedEmptyCell = emptyCell.copy(visibility = Visibility.Shown, board = updatedBoard)

        updatedBoard.cells.revealed.empty shouldBe Set(revealedEmptyCell)
      }
    }
    describe("when revealed bomb cell") {
      it("should not contain bomb cell") {
        val board = Board(Dimensions(3, 3), 2)

        board.cells.revealed.empty shouldBe Set()

        val bombCell = board.cells.withBomb.head

        val updatedBoard = board.revealCell(bombCell.coordinates)

        val revealedBombCell = bombCell.copy(visibility = Visibility.Shown)

        updatedBoard.cells.revealed.empty should not contain revealedBombCell
      }
    }
  }

  describe("Remaining empty cells") {
    describe("when revealed empty cell") {
      it("should be removed from remaining empty cells") {
        val board = Board(Dimensions(3, 3), 2)

        val emptyCell = board.cells.empty.head

        val updatedBoard = board.revealCell(emptyCell.coordinates)

        updatedBoard.cells.hidden.empty shouldNot contain(emptyCell.copy(board = updatedBoard))
      }
    }
  }
}
