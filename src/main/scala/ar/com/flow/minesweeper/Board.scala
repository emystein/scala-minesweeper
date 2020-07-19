package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.Shown

import scala.util.Random

object CellData {
  def apply(cell: Cell): CellData = new CellData(cell.coordinates, cell.content, cell.visibility, cell.mark)
}

case class CellData(coordinates: CartesianCoordinates, content: Option[CellContent] = None, visibility: Visibility = Visibility.Hidden, mark: Option[String] = None)

object Board {
  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions, totalBombs, cellsByCoordinates(dimensions, totalBombs))
  }

  def cellsByCoordinates(dimensions: Dimensions, totalBombs: Int): Map[CartesianCoordinates, CellData] = {
    val allCoordinates = this.allCoordinates(dimensions.rows, dimensions.columns)

    val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        val coordinates = CartesianCoordinates(row, column)
        val hasBomb = bombCoordinates.contains(coordinates)
        coordinates -> CellData(coordinates, CellContent(hasBomb))
      }
    }.toMap
  }

  def allCoordinates(totalRows: Int, totalColumns: Int): Seq[CartesianCoordinates] = {
    for {
      row <- 1 to totalRows
      column <- 1 to totalColumns
    } yield {
      CartesianCoordinates(row, column)
    }
  }
}

class CellInBoard(val cellData: CellData, val board: Board) extends Cell(cellData.coordinates, cellData.content, cellData.visibility, cellData.mark) with RectangleCoordinates {
  override val dimensions: Dimensions = board.dimensions

  def adjacentCells(): Seq[CellInBoard] = {
    neighboursOf(cellData.coordinates).map(board.cellAt)
  }

  def adjacentEmptySpace(previouslyTraversed: Set[CellInBoard] = Set.empty): Set[CellInBoard] = {
    val adjacentCells = this.adjacentCells().toSet -- previouslyTraversed

    adjacentCells.filter(_.content.isEmpty)
      .foldLeft(previouslyTraversed + this)((traversed, adjacent) => adjacent.adjacentEmptySpace(traversed))
  }
}

case class Board(dimensions: Dimensions, totalBombs: Int, cellsByCoordinates: Map[CartesianCoordinates, CellData]) extends RectangleCoordinates {
  def cells: Cells[CellInBoard] = Cells(cellsByCoordinates.values.toSet.map((cellData: CellData) => new CellInBoard(cellData, this)))

  def cellAt(coordinates: CartesianCoordinates): CellInBoard = {
    new CellInBoard(cellsByCoordinates(coordinates), this)
  }

  def setCellValue(coordinates: CartesianCoordinates, value: Option[String]): Board = {
    Board(dimensions, totalBombs, cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(mark = value)))
  }

  def revealCell(coordinates: CartesianCoordinates): Board = {
    copy(cellsByCoordinates = cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(visibility = Shown)))
  }

}
