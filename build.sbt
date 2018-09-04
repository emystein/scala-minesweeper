val ScalatraVersion = "2.6.3"

organization := "ar.com.flow"

name := "Minesweeper"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.6"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.5.2",
  "com.typesafe.slick"      %% "slick"             % "3.2.1",
  "com.h2database"          %  "h2"                % "1.4.196",
  "com.mchange"             %  "c3p0"              % "0.9.5.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.9.v20180320" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.mockito" %% "mockito-scala" % "0.4.2" % "test"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
