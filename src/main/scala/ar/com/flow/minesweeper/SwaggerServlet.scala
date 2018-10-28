package ar.com.flow.minesweeper

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, NativeSwaggerBase, Swagger}

class SwaggerServlet(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase

object MinesweeperApiInfo extends ApiInfo(
  "The Minesweeper API",
  "Docs for the Minesweeper API",
  "http://scalatra.org",
  "apiteam@scalatra.org",
  "MIT",
  "http://opensource.org/licenses/MIT")

class MinesweeperSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", MinesweeperApiInfo)
