import ar.com.flow.minesweeper.persistence.{Persistence, Tables}
import ar.com.flow.minesweeper.rest.{MinesweeperServlet, MinesweeperSwagger, SwaggerServlet}
import javax.servlet.ServletContext
import org.scalatra._
import org.slf4j.LoggerFactory

class ScalatraBootstrap extends LifeCycle with Persistence {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val swagger = new MinesweeperSwagger

  override def init(context: ServletContext) {
    // TODO: move out
    database.run(Tables.createDatabase)

    context.mount(new MinesweeperServlet(database, swagger), "/*")
    context.mount(new SwaggerServlet(), "/api-docs")
  }

  private def closeDbConnection() {
    logger.info("Closing DB connection")
    dataSource.close
  }

  override def destroy(context: ServletContext) {
    super.destroy(context)
    closeDbConnection
  }
}
