package ar.com.flow.minesweeper

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.scalatra.test.scalatest._
import slick.jdbc.H2Profile.api._

class MinesweeperServletTests extends ScalatraFunSuite {
  val cpds = new ComboPooledDataSource
  val database = Database.forDataSource(cpds, None)
  implicit val swagger = new MinesweeperSwagger

  addServlet(new MinesweeperServlet(database, swagger), "/*")

  test("GET /games on MinesweeperServlet should return status 200") {
    get("/games") {
      status should equal (200)
    }
  }

  override def header = ???
}
