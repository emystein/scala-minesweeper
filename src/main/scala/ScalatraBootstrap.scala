import ar.com.flow.minesweeper._
import com.mchange.v2.c3p0.ComboPooledDataSource
import org.scalatra._
import slick.jdbc.H2Profile.api._
import javax.servlet.ServletContext
import org.slf4j.LoggerFactory

class ScalatraBootstrap extends LifeCycle {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val swagger = new MinesweeperSwagger

  val cpds = new ComboPooledDataSource

  logger.info("Created c3p0 connection pool")

  override def init(context: ServletContext) {
    val db = Database.forDataSource(cpds, None)

    // TODO: move out
    db.run(Tables.createDatabase)

    context.mount(new MinesweeperServlet(db, swagger), "/*")
    context.mount(new SwaggerServlet(), "/api-docs")
  }

  private def closeDbConnection() {
    logger.info("Closing c3po connection pool")
    cpds.close
  }

  override def destroy(context: ServletContext) {
    super.destroy(context)
    closeDbConnection
  }
}
