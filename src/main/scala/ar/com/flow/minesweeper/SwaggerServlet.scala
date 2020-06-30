package ar.com.flow.minesweeper

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, ContactInfo, LicenseInfo, NativeSwaggerBase, Swagger}

class SwaggerServlet(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase

object MinesweeperApiInfo extends ApiInfo(
  "The Minesweeper API",
  "Docs for the Minesweeper API",
  "http://scalatra.org",
  ContactInfo("Emiliano Menéndez", "https://github.com/emystein", "emystein@gmail.com"),
  LicenseInfo("MIT", "http://opensource.org/licenses/MIT")
)

class MinesweeperSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", MinesweeperApiInfo)
