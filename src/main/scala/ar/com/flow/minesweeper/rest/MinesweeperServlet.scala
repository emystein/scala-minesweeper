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

  val createGame = (apiOperation[GameResource]("createGame")
    summary ("Create a Game")
    consumes ("application/json")
    produces ("application/json")
    parameter bodyParam[NewGameRequestBody]
    )

  post("/games", operation(createGame)) {
    response.setStatus(201)
    val parameters = parsedBody.extract[NewGameRequestBody]
    save(Game(parameters.rows, parameters.columns, parameters.bombs))
  }

  val markCell = (apiOperation[GameResource]("markCell")
    summary ("Every subsequent request toggles a mark in a Cell in cyclic order: Flag, Question, Clear mark.")
    produces ("application/json")
    parameters(
    pathParam[String]("gameId"),
    pathParam[Int]("row"),
    pathParam[Int]("column")
  ))

  post("/games/:gameId/cell/:row/:column/toggle-mark", operation(markCell)) {
    persistOnCell((game, cellCoordinates) => game.toggleCellMark(cellCoordinates))
  }

  val revealCell = (apiOperation[GameResource]("revealCell")
    summary ("Reveal a Cell")
    produces ("application/json")
    parameters(
    pathParam[String]("gameId"),
    pathParam[Int]("row"),
    pathParam[Int]("column")
  ))

  post("/games/:gameId/cell/:row/:column/reveal", operation(revealCell)) {
    persistOnCell((game, cellCoordinates) => game.revealCell(cellCoordinates))
  }

  val pauseResumeGame = (apiOperation[GameResource]("pauseResumeGame")
    summary ("Pause/Resume a Game")
    produces ("application/json")
    parameters(
    pathParam[String]("gameId")
    ))

  post("/games/:gameId/pause-resume", operation(pauseResumeGame)) {
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
