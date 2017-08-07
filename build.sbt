

lazy val root = (project in file(".")).enablePlugins(PlayScala)

val akkaVersion = "2.3.12"
val json4sVersion = "3.2.11"
val slf4jVersion = "1.7.12"
val projectMainClass = Some("Main")

scalaVersion := "2.11.11"

name := "davebot"

organization := "com.macroservices"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  filters,
  ws,
  jdbc,
  cache,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.json4s" %% "json4s-jackson" % json4sVersion,
  "org.json4s" %% "json4s-ext" % json4sVersion,
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test,
  "org.scalatest" %% "scalatest" % "2.2.1" % Test,
  "org.scalatestplus" %% "play" % "1.4.0-M3" % Test
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"


javaOptions in Test ++= Seq("-Dconfig.file=src/test/resources/application.conf")

mainClass in run in Compile := projectMainClass

assemblyJarName in assembly := "davebot.jar"

//assemblySettings

mainClass in assembly := projectMainClass

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

//addCommandAlias("coverage", "scoverage:test")
