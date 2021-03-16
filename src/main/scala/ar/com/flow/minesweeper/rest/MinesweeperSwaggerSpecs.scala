package ar.com.flow.minesweeper.rest

import org.scalatra.swagger._

trait MinesweeperSwaggerSpecs extends SwaggerSupport {
  protected val applicationDescription = "The Minesweeper API"

  val getGamesSpec = operation((apiOperation[List[GameResource]]("getGames") summary "Show all games"))

  val startNewGameSpec = operation((apiOperation[GameResource]("startNewGame")
    summary ("Start a new Game")
    consumes ("application/json")
    produces ("application/json")
    parameter bodyParam[NewGameRequestBody]
    ))

  val markCellSpec = cellMutatingOperationSpec("markCell",
    "Every subsequent request toggles a mark in a Cell in cyclic order: Flag, Question, Clear mark.")

  val revealCellSpec = cellMutatingOperationSpec("revealCell", "Reveal a Cell")

  val pauseResumeGameSpec = gameMutatingOperationSpec("pauseResumeGame", "Pause/Resume a Game")

  private def gameMutatingOperationSpec(operationName: String, operationSummary: String) = {
    operation(
      (apiOperation[GameResource](operationName)
        summary (operationSummary)
        produces ("application/json")
        parameter (pathParam[String]("gameId"))
        )
    )
  }

  private def cellMutatingOperationSpec(operationName: String, operationSummary: String) = {
    operation(
      (apiOperation[GameResource](operationName)
        summary (operationSummary)
        produces ("application/json")
        parameters(
        pathParam[String]("gameId"),
        pathParam[Int]("row"),
        pathParam[Int]("column")
      ))
    )
  }

}
