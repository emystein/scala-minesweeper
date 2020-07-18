package ar.com.flow.minesweeper

case class CartesianCoordinates(x: Int, y: Int) extends Ordered[CartesianCoordinates] {
  // https://stackoverflow.com/a/19348339/545273
  import scala.math.Ordered.orderingToOrdered

  override def compare(that: CartesianCoordinates): Int = (this.x, this.y) compare (that.x, that.y)
}
