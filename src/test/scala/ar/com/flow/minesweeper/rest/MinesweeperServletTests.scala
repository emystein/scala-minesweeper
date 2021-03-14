package ar.com.flow.minesweeper.rest

import ar.com.flow.minesweeper.Dimensions
import ar.com.flow.minesweeper.persistence.Persistence
import org.json4s._
import org.json4s.jackson.Serialization.read
import org.scalatra.test.scalatest._


class MinesweeperServletTests extends ScalatraFunSuite with Persistence {
  implicit val formats = DefaultFormats
  implicit val swagger = new MinesweeperSwagger

  addServlet(new MinesweeperServlet(database, swagger), "/*")

  test("GET /games should return status 200") {
    get("/games") {
      status should equal (200)
    }
  }

  test("POST /games should create a Game") {
    post("/games", "{ \"rows\":3 , \"columns\": 2, \"bombs\":1 }") {
      status should equal (201)
      val responseGame = read[GameResource](body)
      responseGame.board.dimensions should equal(Dimensions(rows = 3, columns = 2))
    }
  }

  override def header = ???
}
