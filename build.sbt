name := "status"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-simple" % "1.7.30",

  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "com.h2database" % "h2" % "1.4.200",

  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.3",

  "io.circe" %% "circe-yaml" % "0.12.0",
  "io.circe" %% "circe-generic" % "0.12.0",

  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "org.seleniumhq.selenium" % "selenium-java" % "3.141.59",
  "org.seleniumhq.selenium" % "selenium-firefox-driver" % "3.141.59",

  "org.scalatra" %% "scalatra" % "2.7.0-RC1",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.26.v20200117" % "container;compile",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.scalatra" %% "scalatra-json" % "2.7.0-RC1",
  "org.json4s" %% "json4s-jackson" % "3.7.0-M2",
)
