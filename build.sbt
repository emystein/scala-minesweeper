val ScalatraVersion = "2.7.0"
val json4sVersion = "3.6.9"

organization := "ar.com.flow"
name := "minesweeper"
version := "0.1.0-SNAPSHOT"

scalaVersion := "2.13.2"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.scalatra" %% "scalatra-swagger" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % json4sVersion,
  "org.json4s"   %% "json4s-native" % json4sVersion,
  "org.webjars" % "swagger-ui" % "3.19.4",
  "com.typesafe.slick"      %% "slick"             % "3.3.2",
  "com.h2database"          %  "h2"                % "1.4.200",
  "com.zaxxer" % "HikariCP" % "3.4.5",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.9.v20180320" % "container;compile",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  // TODO use webjars-locator-core
  // "org.webjars" % "webjars-locator-core" % "0.46",

  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "org.mockito" %% "mockito-scala" % "1.14.8" % "test"
)

enablePlugins(ScalatraPlugin)

// Tests fails with DB if run in parallel
Test / parallelExecution := false