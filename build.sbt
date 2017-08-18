import play.core.PlayVersion

lazy val plugins: Seq[Plugins] = Seq(play.sbt.PlayScala)

lazy val playSettings: Seq[Setting[_]] = Seq.empty

lazy val scoverageSettings: Seq[Def.Setting[_ >: String with Double with Boolean]] = {
  import scoverage._

  val ScoverageExclusionPatterns = List(
    "<empty>",
    "testOnlyDoNotUseInAppConf.*")
  Seq(
    ScoverageKeys.coverageExcludedPackages := ScoverageExclusionPatterns.mkString("", ";", ""),
    ScoverageKeys.coverageMinimum := 85,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .enablePlugins(Seq(play.sbt.PlayScala) ++ plugins: _*)
  .settings(playSettings ++ scoverageSettings: _*)
  .settings(
    scalaVersion := "2.11.11",
    crossScalaVersions := Seq("2.11.11"),
    parallelExecution in Test := false,
    fork in Test := false,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    routesGenerator := StaticRoutesGenerator,
    ivyScala := ivyScala.value map {
      _.copy(overrideScalaVersion = true)
    },
    resolvers += "emueller-bintray" at "http://dl.bintray.com/emueller/maven",
    resolvers += Resolver.jcenterRepo,
    resolvers += "spray repo" at "http://repo.spray.io"
  )

scalaVersion := "2.11.11"

name := "team10bot"

organization := "com.macroservices"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  filters,
  ws,
  jdbc,
  cache,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.github.fge" % "json-schema-validator" % "2.2.6",
  "com.github.nscala-time" %% "nscala-time" % "2.0.0",
  "org.json4s" %% "json4s-jackson" % "3.2.11",
  "org.json4s" %% "json4s-ext" % "3.2.11",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe" % "config" % "1.3.0",
  "com.h2database" % "h2" % "1.4.192",

  "org.scalatest" %% "scalatest" % "2.2.6" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test,
  "org.pegdown" % "pegdown" % "1.6.0" % Test,
  "org.jsoup" % "jsoup" % "1.8.1" % Test,
  "org.mockito" % "mockito-all" % "1.10.19" % Test,
  "com.typesafe.play" %% "play-test" % PlayVersion.current % Test
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"


//javaOptions in Test ++= Seq("-Dconfig.file=src/test/resources/application.conf")

//mainClass in run in Compile := projectMainClass

//assemblyJarName in assembly := "team10bot.jar"

//assemblySettings

//mainClass in assembly := projectMainClass

//assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

//addCommandAlias("coverage", "scoverage:test")
