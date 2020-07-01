import ar.com.flow.minesweeper._
import javax.servlet.ServletContext
import org.scalatra._
import org.slf4j.LoggerFactory

class ScalatraBootstrap extends LifeCycle with Persistence {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val swagger = new MinesweeperSwagger

  logger.info("Created DB connection pool")

  override def init(context: ServletContext) {
    // TODO: move out
    database.run(Tables.createDatabase)

    context.mount(new MinesweeperServlet(database, swagger), "/*")
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
