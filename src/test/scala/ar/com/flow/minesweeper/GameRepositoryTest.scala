package ar.com.flow.minesweeper

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.scalatest.{FunSuite, Matchers}
import slick.jdbc.H2Profile.api._

class GameRepositoryTest extends FunSuite with DbSchemaSetup with Matchers {
  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  val db = Database.forDataSource(new ComboPooledDataSource, None)
  val gameRepository = new GameRepository(db)

  test("Save new Game") {
    val game: Game = GameFactory.createGame(2, 2, 2)

    gameRepository.save(game)

    val result = gameRepository.findById(game.id)

    result.map { retrievedGame =>
      retrievedGame shouldBe game
    }
  }
}
