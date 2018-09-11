package ar.com.flow.minesweeper

import java.time.LocalDateTime
import java.util.UUID

object GameFactory {
  def createGame(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, LocalDateTime.now, BoardFactory(totalRows, totalColumns, totalBombs))
  }
}

// TODO: Make board a val
class Game(val id: String, val createdAt: LocalDateTime, var board: Board) {
  private val emptyCellFinder = new AdjacentEmptyCellFinder(board)

  def flagCell(coordinates: (Int, Int)): Unit = {
    setCellValue(coordinates, CellValue.flag)
  }

  def questionCell(coordinates: (Int, Int)): Unit = {
    setCellValue(coordinates, CellValue.question)
  }

  def setCellValue(coordinates: (Int, Int), value: String): Unit = {
    board = board.setCellValue(coordinates, value)
  }

  def revealCell(coordinates: (Int, Int)): Unit = {
    val cell = board.getCell(coordinates)

    board = board.revealCell(cell)

    if (!cell.hasBomb) {
      emptyCellFinder.traverseEmptyAdjacentCells(cell).foreach(cell => board = board.revealCell(cell))
    }
  }

  def state: GameState = GameState(board)

  def canEqual(other: Any): Boolean = other.isInstanceOf[Game]

  override def equals(other: Any): Boolean = other match {
    case that: Game => (that canEqual this) && hashCode == that.hashCode
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(createdAt, board)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
