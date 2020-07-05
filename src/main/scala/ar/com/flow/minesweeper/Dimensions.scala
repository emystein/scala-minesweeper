package ar.com.flow.minesweeper

object Dimensions {
  def apply(rows: Int, columns: Int): Dimensions = {
    require(rows > 0)
    require(columns > 0)
    new Dimensions(rows, columns)
  }
}

case class Dimensions(rows: Int, columns: Int)