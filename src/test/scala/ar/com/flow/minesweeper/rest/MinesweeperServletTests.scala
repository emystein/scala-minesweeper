package ar.com.flow.minesweeper.rest

import ar.com.flow.minesweeper.{CartesianCoordinates, Dimensions, Game}
import ar.com.flow.minesweeper.persistence.{DbSchemaSetup, GameRepository, Persistence}
import org.json4s._
import org.json4s.jackson.Serialization.read
import org.scalatra.test.scalatest._


class MinesweeperServletTests extends ScalatraFunSuite with DbSchemaSetup with Persistence {
  val gameRepository = new GameRepository(database)

  implicit val formats = DefaultFormats
  implicit val swagger = new MinesweeperSwagger

  addServlet(new MinesweeperServlet(database, swagger), "/*")

  test("GET /games should return status 200") {
    get("/games") {
      status should equal(200)
    }
  }

  test("Create a new Game") {
    post("/games", "{ \"rows\":3 , \"columns\": 2, \"bombs\":1 }") {
      status should equal(201)

      val createdGame = read[GameResource](body)

      createdGame.board.dimensions shouldBe (Dimensions(rows = 3, columns = 2))
      createdGame.board.cells.count(_.hasBomb) shouldBe (1)
      createdGame.state shouldBe ("playing")
      createdGame.result shouldBe ("pending")
    }
  }

  test("Reveal Cell") {
    val game: Game = Game(2, 2, 2)
    val gameRepository = new GameRepository(database)
    gameRepository.save(game)

    post(s"/games/${game.id}/cell/1/1/reveal") {
      status should equal(200)

      val updatedGame = read[GameResource](body)

      updatedGame.board.cells.find(c => c.coordinates == CartesianCoordinates(1, 1)).head.visibility shouldBe ("revealed")
    }
  }

  test("Toggle Mark on Cell") {
    val game: Game = Game(2, 2, 2)
    val gameRepository = new GameRepository(database)
    gameRepository.save(game)

    post(s"/games/${game.id}/cell/1/1/toggle-mark") {
      status should equal(200)

      val updatedGame = read[GameResource](body)

      updatedGame.board.cells.find(c => c.coordinates == CartesianCoordinates(1, 1)).head.mark shouldBe Some("f")
    }

    post(s"/games/${game.id}/cell/1/1/toggle-mark") {
      status should equal(200)

      val updatedGame = read[GameResource](body)

      updatedGame.board.cells.find(c => c.coordinates == CartesianCoordinates(1, 1)).head.mark shouldBe Some("?")
    }
  }

  test("Toggle Pause on Game") {
    val game: Game = Game(2, 2, 2)
    gameRepository.save(game)
    togglePauseResume(game.id, expectedState = "paused")
    togglePauseResume(game.id, expectedState = "playing")
  }

  def togglePauseResume(gameId: String, expectedState: String): Unit = {
    post(s"/games/${gameId}/pause-resume") {
      status should equal(200)

      val updatedGame = read[GameResource](body)

      updatedGame.state shouldBe expectedState
    }
  }

  override def header = ???
}
