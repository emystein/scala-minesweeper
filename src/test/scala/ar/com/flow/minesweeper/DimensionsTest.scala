package ar.com.flow.minesweeper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class DimensionsTest extends AnyFunSuite with Matchers {
  test("New board should have more than 0 rows") {
    Range.inclusive(-1, 0).foreach(row =>
      assertThrows[IllegalArgumentException] {
        Dimensions(row, 3)
      }
    )
  }

  test("Dimensions have more than 0 columns") {
    Range.inclusive(-1, 0).foreach(column =>
      assertThrows[IllegalArgumentException] {
        Dimensions(3, column)
      }
    )
  }
}
