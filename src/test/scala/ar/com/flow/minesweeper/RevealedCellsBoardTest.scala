package ar.com.flow.minesweeper

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class RevealedCellsBoardTest extends AnyFunSpec with Matchers {
  describe("Revealed cells") {
    describe("when creating a Board") {
      it("should be an empty set") {
        val board = Board(Dimensions(3, 3), 2)

        board.cells.revealed shouldBe Set.empty
      }
    }
    describe("when revealing a cell") {
      describe("not previously revealed") {
        it("should add cell to revealed cell set") {
          val board = Board(Dimensions(3, 3), 2)

          board.revealCell(1, 1)

          board.cells.revealed shouldBe Set(board.getCell(1, 1))
        }
      }
      describe("already revealed") {
        it("should not add cell to revealed cell set") {
          val board = Board(Dimensions(3, 3), 2)

          board.revealCell(1, 1)
          board.revealCell(1, 1)

          board.cells.revealed shouldBe Set(board.getCell(1, 1))
        }
      }
    }
  }

  describe("Revealed empty cells") {
    describe("when revealed empty cell") {
      it("should contain empty cell") {
        val board = Board(Dimensions(3, 3), 2)

        board.cells.revealedEmpty shouldBe Set()

        val emptyCell = board.cells.empty.head

        board.revealCell(emptyCell.row, emptyCell.column)

        val revealedEmptyCell = emptyCell.copy(isRevealed = true)

        board.cells.revealedEmpty shouldBe Set(revealedEmptyCell)
      }
    }
    describe("when revealed bomb cell") {
      it("should not contain bomb cell") {
        val board = Board(Dimensions(3, 3), 2)

        board.cells.revealedEmpty shouldBe Set()

        val bombCell = board.cells.withBomb.head

        board.revealCell(bombCell.row, bombCell.column)

        val revealedBombCell = bombCell.copy(isRevealed = true)

        board.cells.revealedEmpty should not contain revealedBombCell
      }
    }
  }

  describe("Remaining empty cells") {
    describe("when revealed empty cell") {
      it("should be removed from remaining empty cells") {
        val board = Board(Dimensions(3, 3), 2)

        val emptyCell = board.cells.empty.head

        val newBoard = board.revealCell(emptyCell.row, emptyCell.column)

        newBoard.cells.notRevealedEmpty shouldBe board.cells.notRevealedEmpty - emptyCell
      }
    }
  }
}
