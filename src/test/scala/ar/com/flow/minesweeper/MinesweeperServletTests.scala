package ar.com.flow.minesweeper

import slick.jdbc.H2Profile.api._
import com.mchange.v2.c3p0.ComboPooledDataSource
import org.scalatra.test.scalatest._

class MinesweeperServletTests extends ScalatraFunSuite {
  val cpds = new ComboPooledDataSource
  val database = Database.forDataSource(cpds, None)
  implicit val swagger = new MinesweeperSwagger

  addServlet(new MinesweeperServlet(database, swagger), "/*")

  test("GET / on MinesweeperServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
