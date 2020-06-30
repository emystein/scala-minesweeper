package ar.com.flow.minesweeper

import org.scalatra._

trait MyScalatraWebAppStack extends ScalatraServlet {
  get("/swagger-ui/*") {
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