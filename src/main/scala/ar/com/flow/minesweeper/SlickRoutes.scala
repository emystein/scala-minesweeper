package ar.com.flow.minesweeper

import org.scalatra.{FutureSupport, ScalatraBase}
import slick.jdbc.H2Profile.api._

trait SlickRoutes extends ScalatraBase with FutureSupport {
  def db: Database

  get("/db/create") {
    db.run(Tables.createDatabase)
  }

  get("/db/drop") {
    db.run(Tables.dropSchemaAction)
  }
}