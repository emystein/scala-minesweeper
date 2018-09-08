package ar.com.flow.minesweeper

import java.util.{Date, UUID}

object GameFactory {
  def createGame(totalRows: Int, totalColumns: Int, totalBombs: Int): Game = {
    new Game(UUID.randomUUID().toString, new Date(), BoardFactory(totalRows, totalColumns, totalBombs))
  }
}

// TODO: Make board a val
class Game(val id: String, val createdAt: java.util.Date, var board: Board) {
  private val cellLocationContext = new CellLocationContext(board.totalRows, board.totalColumns)

  def flagCell(coordinates: (Int, Int)): Unit = {
    board = board.setCellValue(coordinates, CellValue.flag)
  }

  def questionCell(coordinates: (Int, Int)): Unit = {
    board = board.setCellValue(coordinates, CellValue.question)
  }

  def revealCell(coordinates: (Int, Int)): Unit = {
    val cell = board.getCell(coordinates)

    revealEmptyAdjacentCells(cell)
  }

  def revealEmptyAdjacentCells(cell: Cell, previouslyTraversed: Set[Cell] = Set.empty): Set[Cell] = {
      board = board.revealCell(cell)

      val adjacentCells = adjacentCellsOf(cell).toSet -- previouslyTraversed

      adjacentCells.filter(!_.hasBomb)
        .foldLeft(previouslyTraversed + cell)((traversed, adjacent) => revealEmptyAdjacentCells(adjacent, traversed))
  }

  private def adjacentCellsOf(cell: Cell): Seq[Cell] = {
    cellLocationContext.neighboursOf(cell.row, cell.column).map(board.getCell)
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
