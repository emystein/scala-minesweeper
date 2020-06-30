package ar.com.flow.minesweeper

import java.time.LocalDateTime

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameRepositoryTest extends AnyFunSuite with DbSchemaSetup with Persistence with Matchers {
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
}
