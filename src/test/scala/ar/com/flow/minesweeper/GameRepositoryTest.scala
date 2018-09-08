package ar.com.flow.minesweeper

import java.time.LocalDateTime

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.scalatest.{FunSuite, Matchers}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameRepositoryTest extends FunSuite with DbSchemaSetup with Matchers {
  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  val db = Database.forDataSource(new ComboPooledDataSource, None)
  val gameRepository = new GameRepository(db)

  test("Save new Game") {
    val dateTime: java.time.LocalDateTime = LocalDateTime.now

    val game: Game = GameFactory.createGame(2, 2, 2)

    gameRepository.save(game)

    val result = gameRepository.findById(game.id)

    val retrievedGame = Await.result(result, Duration.Inf)
    retrievedGame shouldBe game
    retrievedGame.createdAt.isEqual(dateTime) || retrievedGame.createdAt.isAfter(dateTime) shouldBe true
  }
}
