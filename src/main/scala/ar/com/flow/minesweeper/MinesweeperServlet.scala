package ar.com.flow.minesweeper

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json.JacksonJsonSupport
import slick.jdbc.H2Profile.api._

class MinesweeperServlet(val db: Database) extends ScalatraServlet with JacksonJsonSupport with FutureSupport with SlickRoutes {
  val gameRepository = new GameRepository(db)

  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  // Sets up automatic case class to JSON output serialization, required by the JValueResult trait.
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

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

    saveAndReturn(game)
  }

  post("/games/:gameId/cell/:row/:column/question") {
    val game = gameRepository.findById(params("gameId"))
    val x = params("row").toInt
    val y = params("column").toInt
    game.questionCell(x, y)

    saveAndReturn(game)
  }

  post("/games/:gameId/cell/:row/:column/flag") {
    val game = gameRepository.findById(params("gameId"))
    val x = params("row").toInt
    val y = params("column").toInt
    game.flagCell(x, y)

    saveAndReturn(game)
  }

  post("/games/:gameId/cell/:row/:column/reveal") {
    val game = gameRepository.findById(params("gameId"))
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
