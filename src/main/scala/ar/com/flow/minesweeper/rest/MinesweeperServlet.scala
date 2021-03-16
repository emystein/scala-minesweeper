package ar.com.flow.minesweeper.rest

import ar.com.flow.minesweeper._
import ar.com.flow.minesweeper.persistence.GameRepository
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger._
import org.scalatra.util.MimeTypes
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

class MinesweeperServlet(val db: Database, implicit val swagger: Swagger) extends SwaggerUiRoute with NativeJsonSupport with SwaggerSupport with FutureSupport with DbRoutes {
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

  get("/games", operation(getGames)) {
    gameRepository.findAll.map(f => f.map(GameResource.from))
  }

  val postGame = (apiOperation[GameResource]("postGame")
    summary ("Create a Game")
    consumes ("application/json")
    produces ("application/json")
    parameter bodyParam[NewGameRequestBody]
    )

  post("/games", operation(postGame)) {
    response.setStatus(201)
    val parameters = parsedBody.extract[NewGameRequestBody]
    save(Game(parameters.rows, parameters.columns, parameters.bombs))
  }

  post("/games/:gameId/cell/:row/:column/toggle-mark") {
    persistOnCell((game, cellCoordinates) => game.toggleCellMark(cellCoordinates))
  }

  post("/games/:gameId/cell/:row/:column/reveal") {
    persistOnCell((game, cellCoordinates) => game.revealCell(cellCoordinates))
  }

  post("/games/:gameId/pause-resume") {
    gameRepository.findById(params("gameId")).map { game =>
      save(game.togglePauseResume)
    }
  }

  private def persistOnCell(onCell: (Game, CartesianCoordinates) => Game): Future[GameResource] = {
    val cellCoordinates = CartesianCoordinates(params("row"), params("column"))

    gameRepository.findById(params("gameId")).map { game =>
      save(onCell(game, cellCoordinates))
    }
  }

  private def save(game: Game) = {
    gameRepository.save(game)
    GameResource.from(game)
  }
}
