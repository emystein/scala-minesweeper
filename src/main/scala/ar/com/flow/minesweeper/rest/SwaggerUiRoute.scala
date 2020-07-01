package ar.com.flow.minesweeper.rest

import org.scalatra.ScalatraServlet
import org.webjars.WebJarAssetLocator

trait SwaggerUiRoute extends ScalatraServlet {

  get("/swagger-ui/*") {
    // TODO user webjars-locator-core
//  val webJarAssetLocator = new WebJarAssetLocator()
//    val resourcePath = webJarAssetLocator.getFullPath("swagger-ui", params("splat"))
    val resourcePath = "/META-INF/resources/webjars/swagger-ui/3.19.4/" + params("splat")
    Option(getClass.getResourceAsStream(resourcePath)) match {
      case Some(inputStream) => {
        contentType = servletContext.getMimeType(resourcePath)
        inputStream.readAllBytes()
      }
      case None => resourceNotFound()
    }
  }
}
