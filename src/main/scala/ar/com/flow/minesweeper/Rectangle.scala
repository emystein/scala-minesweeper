package ar.com.flow.minesweeper

object Dimensions {
  def apply(rows: Int, columns: Int): Dimensions = {
    require(rows > 0)
    require(columns > 0)
    new Dimensions(rows, columns)
  }
}

case class Dimensions(rows: Int, columns: Int)

trait RectangleCoordinates {
  val dimensions: Dimensions

  def adjacentOf(coordinates: CartesianCoordinates): Set[CartesianCoordinates] = {
    for {
      x <- coordinates.x - 1 to coordinates.x + 1
      y <- coordinates.y - 1 to coordinates.y + 1
      if (x > 0 && x <= dimensions.rows) && (y > 0 && y <= dimensions.columns) && (x != coordinates.x || y != coordinates.y)
    } yield CartesianCoordinates(x, y)
  }.toSet
}
