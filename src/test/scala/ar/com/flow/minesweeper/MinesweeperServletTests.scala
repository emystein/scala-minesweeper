package ar.com.flow.minesweeper

import org.scalatra.test.scalatest._

class MinesweeperServletTests extends ScalatraFunSuite {

  addServlet(classOf[MinesweeperServlet], "/*")

  test("GET / on MinesweeperServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
