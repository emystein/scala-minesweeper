package ar.com.flow.minesweeper

import slick.jdbc.H2Profile.api._
import org.scalatra._
import org.scalatra.json.JacksonJsonSupport

import scala.collection.mutable

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

class MinesweeperServlet(val db: Database) extends ScalatraServlet with JacksonJsonSupport with FutureSupport with SlickRoutes {
  val gameRepository = new GameRepository(db)

  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

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
    // games.values.map(GameResource.from)
    gameRepository.findAll.map(GameResource.from)
  }

  post("/games") {
    val parameters = parsedBody.extract[NewGameRequestBody]
    val game = GameFactory.createGame(parameters.rows, parameters.columns, parameters.bombs)
    // TODO: move game id generation from GameFactory.createGame to games repository
    games(game.id) = game

    saveAndReturn(game)
  }

  post("/games/:gameId/cell/:row/:column/question") {
    val game = games(params("gameId"))
    val x = params("row").toInt
    val y = params("column").toInt
    game.questionCell(x, y)

    saveAndReturn(game)
  }

  post("/games/:gameId/cell/:row/:column/flag") {
    val game = games(params("gameId"))
    val x = params("row").toInt
    val y = params("column").toInt
    game.flagCell(x, y)

    saveAndReturn(game)
  }

  post("/games/:gameId/cell/:row/:column/reveal") {
    val game = games(params("gameId"))
    val x = params("row").toInt
    val y = params("column").toInt
    game.revealCell(x, y)

    saveAndReturn(game)
  }

  private def saveAndReturn(game: Game) = {
    gameRepository.save(game)
    GameResource.from(game)
  }
}
