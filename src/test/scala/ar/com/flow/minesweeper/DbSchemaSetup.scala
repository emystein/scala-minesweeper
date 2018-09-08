package ar.com.flow.minesweeper

import org.scalatest.{BeforeAndAfter, TestSuite}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait DbSchemaSetup extends TestSuite with BeforeAndAfter {
  def db: Database

  before {
    Await.result(db.run(Tables.createDatabase), Duration.Inf)
  }

  after {
    Await.result(db.run(Tables.dropSchemaAction), Duration.Inf)
  }
}
