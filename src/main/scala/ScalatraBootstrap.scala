import ar.com.flow.minesweeper._
import javax.servlet.ServletContext
import org.scalatra._
import org.slf4j.LoggerFactory
import slick.jdbc.H2Profile.api._

class ScalatraBootstrap extends LifeCycle with Persistence {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val swagger = new MinesweeperSwagger

  logger.info("Created DB connection pool")

  override def init(context: ServletContext) {
    val db = Database.forDataSource(dataSource, None)

    // TODO: move out
    db.run(Tables.createDatabase)

    context.mount(new MinesweeperServlet(db, swagger), "/*")
    context.mount(new SwaggerServlet(), "/api-docs")
  }

  private def closeDbConnection() {
    logger.info("Closing c3po connection pool")
    dataSource.close
  }

  override def destroy(context: ServletContext) {
    super.destroy(context)
    closeDbConnection
  }
}
