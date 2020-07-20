package ar.com.flow.minesweeper

object CartesianCoordinates {
  def all(totalInXAxis: Int, totalInYAxis: Int): Seq[CartesianCoordinates] = {
    for {
      x <- 1 to totalInXAxis
      y <- 1 to totalInYAxis
    } yield {
      CartesianCoordinates(x, y)
    }
  }
}

case class CartesianCoordinates(x: Int, y: Int) extends Ordered[CartesianCoordinates] {
  // https://stackoverflow.com/a/19348339/545273
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: CartesianCoordinates): Int = (this.x, this.y) compare (that.x, that.y)
}
