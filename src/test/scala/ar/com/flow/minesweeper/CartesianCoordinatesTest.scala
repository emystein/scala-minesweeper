package ar.com.flow.minesweeper

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CartesianCoordinatesTest extends AnyWordSpec with Matchers {
  "Coordinates" should {
    "have x and y" in {
      val coordinates = CartesianCoordinates(1, 2)
      coordinates.x shouldBe 1
      coordinates.y shouldBe 2
    }
  }
}
