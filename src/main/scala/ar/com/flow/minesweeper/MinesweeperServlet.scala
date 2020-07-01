package ar.com.flow.minesweeper

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger._
import slick.jdbc.H2Profile.api._

class MinesweeperServlet(val db: Database, implicit val swagger: Swagger) extends MyScalatraWebAppStack with NativeJsonSupport with SwaggerSupport with FutureSupport with SlickRoutes {
  val gameRepository = new GameRepository(db)

  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  // Sets up automatic case class to JSON output serialization, required by the JValueResult trait.
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  protected val applicationDescription = "The Minesweeper API"

  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  val getGames = (apiOperation[List[GameResource]]("getGames") summary "Show all flowers")

  get("/games", operation(getGames)) {
    gameRepository.findAll.map(f => f.map(GameResource.from(_)))
  }

  post("/games") {
    val parameters = parsedBody.extract[NewGameRequestBody]
    val game = Game(parameters.rows, parameters.columns, parameters.bombs)

    saveAndReturn(game)
  }

  post("/games/:gameId/cell/:row/:column/question") {
    gameRepository.findById(params("gameId")).map{game =>
      game.questionCell(cellCoordinates)
      saveAndReturn(game)
    }
  }

  post("/games/:gameId/cell/:row/:column/flag") {
    gameRepository.findById(params("gameId")).map{game =>
      game.flagCell(cellCoordinates)
      saveAndReturn(game)
    }
  }

  post("/games/:gameId/cell/:row/:column/reveal") {
    gameRepository.findById(params("gameId")).map{game =>
      game.revealCell(cellCoordinates)
      saveAndReturn(game)
    }
  }

  post("/games/:gameId/pause") {
    gameRepository.findById(params("gameId")).map{game =>
      game.pause
      saveAndReturn(game)
    }
  }

  post("/games/:gameId/resume") {
    gameRepository.findById(params("gameId")).map{game =>
      game.resume
      saveAndReturn(game)
    }
  }

  private def cellCoordinates(): (Int, Int) = {
    val x = params("row").toInt
    val y = params("column").toInt
    (x, y)
  }

  private def saveAndReturn(game: Game) = {
    gameRepository.save(game)
    GameResource.from(game)
  }
}
