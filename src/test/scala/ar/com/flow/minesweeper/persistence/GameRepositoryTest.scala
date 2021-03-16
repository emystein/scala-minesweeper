package ar.com.flow.minesweeper.persistence

import ar.com.flow.minesweeper._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.LocalDateTime
import java.util.UUID
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameRepositoryTest extends AnyFunSuite with DbSchemaSetup with Persistence with Matchers with GameMatchers {
  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  val gameRepository = new GameRepository(database)

  test("Save new Game") {
    val dateTime: java.time.LocalDateTime = LocalDateTime.now

    val game: Game = Game(2, 2, 2)

    gameRepository.save(game)

    val result = gameRepository.findById(game.id)

    val retrievedGame = Await.result(result, Duration.Inf)
    retrievedGame shouldBe game
    retrievedGame.createdAt.isEqual(dateTime) || retrievedGame.createdAt.isAfter(dateTime) shouldBe true
  }

  test("Not marked Cell") {
    val game: Game = Game(2, 2, 2)

    gameRepository.save(game)

    val gameFuture = gameRepository.findById(game.id)

    val retrievedGame = Await.result(gameFuture, Duration.Inf)
    retrievedGame.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe None
  }

  test("Toggle Cell mark") {
    val game: Game = Game(2, 2, 2)

    val flagCellGame = game.toggleCellMark(CartesianCoordinates(1, 1))

    gameRepository.save(flagCellGame)

    val gameFuture = gameRepository.findById(game.id)

    val retrievedGame = Await.result(gameFuture, Duration.Inf)
    retrievedGame.board.cellAt(CartesianCoordinates(1, 1)).mark shouldBe Some(CellMark.Flag)
  }

  test("Persist Running Game") {
    val game: Game = Game(2, 2, 2)

    gameRepository.save(game)

    val retrievedGame = Await.result(gameRepository.findById(game.id), Duration.Inf)

    retrievedGame.runningState shouldBe(GameRunningState.Running)
  }

  test("Persist Paused Game") {
    val pausedGame: Game = PausedGame(UUID.randomUUID().toString, LocalDateTime.now, Board(Dimensions(2, 2), 2))

    gameRepository.save(pausedGame)

    val savedPausedGame = Await.result(gameRepository.findById(pausedGame.id), Duration.Inf)

    savedPausedGame should bePaused
  }

  test("Toggle Pause/Resume") {
    val game: Game = Game(2, 2, 2)
    val pausedGame = game.togglePauseResume
    Await.result(gameRepository.save(pausedGame), Duration.Inf)
    val retrievedPausedGame = Await.result(gameRepository.findById(game.id), Duration.Inf)
    retrievedPausedGame should bePaused
    val resumedGame = pausedGame.togglePauseResume
    Await.result(gameRepository.save(resumedGame), Duration.Inf)
    val retrievedResumedGame = Await.result(gameRepository.findById(game.id), Duration.Inf)
    retrievedResumedGame should beRunning
  }
}
