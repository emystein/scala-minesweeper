package ar.com.flow.minesweeper

import org.scalatra._
import org.scalatra.json.JacksonJsonSupport

import scala.collection.mutable

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

class MinesweeperServlet extends ScalatraServlet with JacksonJsonSupport {
  // Sets up automatic case class to JSON output serialization, required by the JValueResult trait.
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  // TODO: Implement repository
  val games = new mutable.HashMap[String, Game]()

  get("/") {
    views.html.hello()
  }

  get("/games") {
    games.values.map(GameResource.from)
  }

  post("/games") {
    val parameters = parsedBody.extract[NewGameRequestBody]
    val game = GameFactory.createGame(parameters.rows, parameters.columns, parameters.bombs)
    // TODO: move game id generation from GameFactory.createGame to games repository
    games(game.id) = game

    GameResource.from(game)
  }

  post("/games/:gameId/cell/:row/:column/question") {
    val game = games(params("gameId"))
    val x = params("row").toInt
    val y = params("column").toInt
    game.questionCell(x, y)

    GameResource.from(game)
  }

  post("/games/:gameId/cell/:row/:column/flag") {
    val game = games(params("gameId"))
    val x = params("row").toInt
    val y = params("column").toInt
    game.flagCell(x, y)

    GameResource.from(game)
  }

  post("/games/:gameId/cell/:row/:column/reveal") {
    val game = games(params("gameId"))
    val x = params("row").toInt
    val y = params("column").toInt
    game.revealCell(x, y)

    GameResource.from(game)
  }
}
