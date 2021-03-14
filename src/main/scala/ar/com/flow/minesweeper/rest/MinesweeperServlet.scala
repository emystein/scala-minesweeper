package ar.com.flow.minesweeper.rest

import ar.com.flow.minesweeper._
import ar.com.flow.minesweeper.persistence.GameRepository
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger._
import org.slf4j.LoggerFactory
import slick.jdbc.H2Profile.api._

class MinesweeperServlet(val db: Database, implicit val swagger: Swagger) extends SwaggerUiRoute with NativeJsonSupport with SwaggerSupport with FutureSupport with DbRoutes {
  val logger = LoggerFactory.getLogger(getClass)

  val gameRepository = new GameRepository(db)

  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  // Sets up automatic case class to JSON output serialization, required by the JValueResult trait.
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  protected val applicationDescription = "The Minesweeper API"

  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  val getGames = (apiOperation[List[GameResource]]("getGames") summary "Show all games")

  override def readJsonFromBody(bd: _root_.scala.Predef.String): org.json4s.JValue = {
    super.readJsonFromBody(bd)
  }

  get("/games", operation(getGames)) {
    gameRepository.findAll.map(f => f.map(GameResource.from(_)))
  }

  post("/games") {
    val parameters = parsedBody.extract[NewGameRequestBody]
    val game = Game(parameters.rows, parameters.columns, parameters.bombs)
    response.setStatus(201)
    save(game)
  }

  post("/games/:gameId/cell/:row/:column/toggle-mark") {
    val coordinates = cellCoordinates()

    logger.debug("Toggling Cell ({}, {}) in Game {}", coordinates.x, coordinates.y, params("gameId"))

    gameRepository.findById(params("gameId")).map { game =>
      save(game.toggleCellMark(coordinates))
    }
  }

  post("/games/:gameId/cell/:row/:column/reveal") {
    val coordinates = cellCoordinates()

    logger.debug("Revealing Cell ({}, {}) in Game {}", coordinates.x, coordinates.y, params("gameId"))

    gameRepository.findById(params("gameId")).map { game =>
      save(game.revealCell(coordinates))
    }
  }

  post("/games/:gameId/pause-resume") {
    gameRepository.findById(params("gameId")).map { game =>
      save(game.togglePauseResume)
    }
  }

  private def cellCoordinates(): CartesianCoordinates = {
    val x = params("row").toInt
    val y = params("column").toInt
    CartesianCoordinates(x, y)
  }

  private def save(game: Game) = {
    gameRepository.save(game)
    GameResource.from(game)
  }
}
