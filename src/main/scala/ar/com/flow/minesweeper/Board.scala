package ar.com.flow.minesweeper

import ar.com.flow.minesweeper.Visibility.Shown

import scala.util.Random

object Board {
  def apply(dimensions: Dimensions, totalBombs: Int): Board = {
    require(totalBombs >= 0)
    Board(dimensions, totalBombs, cellsByCoordinates(dimensions, totalBombs))
  }

  def cellsByCoordinates(dimensions: Dimensions, totalBombs: Int): Map[CartesianCoordinates, Cell] = {
    val allCoordinates = this.allCoordinates(dimensions.rows, dimensions.columns)

    val bombCoordinates = Random.shuffle(allCoordinates).take(totalBombs)

    {
      for {
        row <- 1 to dimensions.rows
        column <- 1 to dimensions.columns
      } yield {
        val coordinates = CartesianCoordinates(row, column)
        val hasBomb = bombCoordinates.contains(coordinates)
        coordinates -> Cell(coordinates, CellContent(hasBomb))
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

case class Board(dimensions: Dimensions, totalBombs: Int, cellsByCoordinates: Map[CartesianCoordinates, Cell]) extends RectangleCoordinates {
  def cells = Cells(cellsByCoordinates.values.toSet)

  def cellAt(coordinates: CartesianCoordinates): Cell = {
    cellsByCoordinates(coordinates)
  }

  def setCellValue(coordinates: CartesianCoordinates, value: Option[String]): Board = {
    Board(dimensions, totalBombs, cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(mark = value)))
  }

  def revealCell(coordinates: CartesianCoordinates): Board = {
    Board(dimensions, totalBombs,
      cellsByCoordinates + (coordinates -> cellsByCoordinates(coordinates).copy(visibility = Shown))
    )
  }

  def cellsAdjacentTo(cell: Cell): Seq[Cell] = {
    neighboursOf(cell.coordinates).map(cellAt)
  }

  def bombsAdjacentTo(cell: Cell): Seq[Cell] = {
    cellsAdjacentTo(cell).filter(_.content.isDefined)
  }

  def emptySpaceAdjacentTo(cell: Cell, previouslyTraversed: Set[Cell] = Set.empty): Set[Cell] = {
    val adjacentCells = cellsAdjacentTo(cell).toSet -- previouslyTraversed

    adjacentCells.filter(_.content.isEmpty)
      .foldLeft(previouslyTraversed + cell)((traversed, adjacent) => emptySpaceAdjacentTo(adjacent, traversed))
  }
}
