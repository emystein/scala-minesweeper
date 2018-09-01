package ar.com.flow.minesweeper

import org.scalatest.{FunSpec, Matchers}

class RevealedCellsBoardTest extends FunSpec with Matchers {
  describe("Revealed cells") {
    describe("when creating a Board") {
      it("should be an empty set") {
        val board = new Board(3, 3, 2)

        board.revealedCells shouldBe Set.empty
      }
    }
    describe("when revealing a cell") {
      describe("not previously revealed") {
        it("should add cell to revealed cell set") {
          val board = new Board(3, 3, 2)

          board.revealCell(1, 1)

          board.revealedCells shouldBe Set(board.getCell(1, 1))
        }
      }
      describe("already revealed") {
        it("should not add cell to revealed cell set") {
          val board = new Board(3, 3, 2)

          board.revealCell(1, 1)
          board.revealCell(1, 1)

          board.revealedCells shouldBe Set(board.getCell(1, 1))
        }
      }
    }
  }

  describe("Revealed empty cells") {
    describe("when revealed empty cell") {
      it("should contain empty cell") {
        val board = new Board(3, 3, 2)

        board.revealedEmptyCells shouldBe Set()

        val emptyCell = board.emptyCells.head

        board.revealCell(emptyCell.row, emptyCell.column)

        val revealedEmptyCell = emptyCell.copy(isRevealed = true)

        board.revealedEmptyCells shouldBe Set(revealedEmptyCell)
      }
    }
    describe("when revealed bomb cell") {
      it("should not contain bomb cell") {
        val board = new Board(3, 3, 2)

        board.revealedEmptyCells shouldBe Set()

        val bombCell = board.bombCells.head

        board.revealCell(bombCell.row, bombCell.column)

        val revealedBombCell = bombCell.copy(isRevealed = true)

        board.revealedEmptyCells should not contain revealedBombCell
      }
    }
  }
}
