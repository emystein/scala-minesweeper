package ar.com.flow.minesweeper

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameRepositoryTest extends FunSuite with BeforeAndAfter with Matchers {
  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  val cpds = new ComboPooledDataSource
  val db = Database.forDataSource(cpds, None)
  val gameRepository = new GameRepository(db)

  before {
    Await.result(db.run(Tables.createDatabase), Duration.Inf)
  }

  after {
    Await.result(db.run(Tables.dropSchemaAction), Duration.Inf)
  }

  test("Save new Game") {
    val game: Game = GameFactory.createGame(2, 2, 2)

    gameRepository.save(game)

    val retrievedGame = gameRepository.findById(game.id)

    retrievedGame.id shouldBe game.id
    retrievedGame.createdAt shouldBe game.createdAt
    retrievedGame.board shouldBe game.board
  }
}
