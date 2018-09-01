package ar.com.flow.minesweeper

import java.util.{Date, UUID}

import org.mockito.integrations.scalatest.MockitoFixture
import org.scalatest.{FunSuite, Matchers}

// TODO: Add precondition for board with at least 1 more empty cell than bombs
class GameTest extends FunSuite with Matchers with MockitoFixture {
  test("Question Cell") {
    val game = GameFactory.createGame(3, 3, 2)

    game.board.getCell(1, 1).value shouldBe CellValue.empty

    game.questionCell(1, 1)

    game.board.getCell(1, 1).value shouldBe CellValue.question
  }

  test("Flag Cell") {
    val game = GameFactory.createGame(3, 3, 2)

    game.board.getCell(1, 1).value shouldBe CellValue.empty

    game.flagCell(1, 1)

    game.board.getCell(1, 1).value shouldBe CellValue.flag
  }

  test("Reveal Cell should mark it as revealed") {
    val game = GameFactory.createGame(3, 3, 2)

    game.board.getCell(1, 1).isRevealed shouldBe false

    game.revealCell(1, 1)

    game.board.getCell(1, 1).isRevealed shouldBe true
  }

  test("Revealing an empty cell and having remaining empty cells should keep the game playing") {
    val board = mock[Board]
    val newBoard = mock[Board]

    val game = new Game(UUID.randomUUID().toString, new Date(), board)

    when(board.revealCell(1, 1)).thenReturn(newBoard)
    val cell1 = new Cell(1, 1, false, 0, true)
    val cell2 = new Cell(1, 2, false, 0, true)
    when(newBoard.getCell(1, 1)).thenReturn(cell1)
    val allEmptyCells = Set(cell1, cell2)
    when(newBoard.revealedEmptyCells).thenReturn(Set(cell1))
    when(newBoard.emptyCells).thenReturn(allEmptyCells)

    game.revealCell(1, 1)

    game.state shouldBe "playing"
  }

  test("Revealing a cell containing a bomb should finish the game as lost") {
    val board = mock[Board]
    val newBoard = mock[Board]

    val game = new Game(UUID.randomUUID().toString, new Date(), board)

    when(board.revealCell(1, 1)).thenReturn(newBoard)
    val bombCell = new Cell(1, 1, true, 0, true)
    when(newBoard.getCell(1, 1)).thenReturn(bombCell)

    game.revealCell(1, 1)

    game.state shouldBe "finished"
    game.result shouldBe "lost"
  }

  test("Revealing last empty cell should finish the game as won") {
    val board = mock[Board]
    val newBoard = mock[Board]
    val game = new Game(UUID.randomUUID().toString, new Date(), board)

    when(board.revealCell(1, 1)).thenReturn(newBoard)
    val cell = new Cell(1, 1, false, 0, true)
    val allEmptyCells = Set(cell)
    when(newBoard.getCell(1, 1)).thenReturn(cell)
    when(newBoard.revealedEmptyCells).thenReturn(allEmptyCells)
    when(newBoard.emptyCells).thenReturn(allEmptyCells)

    game.revealCell(1, 1)

    game.state shouldBe "finished"
    game.result shouldBe "won"
  }
}
