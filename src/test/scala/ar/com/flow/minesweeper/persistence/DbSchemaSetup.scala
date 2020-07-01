package ar.com.flow.minesweeper.persistence

import org.scalatest.{BeforeAndAfter, TestSuite}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait DbSchemaSetup extends TestSuite with BeforeAndAfter {
  def database: Database

  before {
    Await.result(database.run(Tables.createDatabase), Duration.Inf)
  }

  after {
    Await.result(database.run(Tables.dropSchemaAction), Duration.Inf)
  }
}
