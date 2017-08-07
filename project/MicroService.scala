import play.routes.compiler.StaticRoutesGenerator
import play.sbt.routes.RoutesKeys.routesGenerator
import sbt.Keys.{resolvers, _}
import sbt.Tests.{Group, SubProcess}
import sbt._

trait MicroService {

//  import DefaultBuildSettings._
  import TestPhases._
  import play.sbt.PlayScala

  lazy val appDependencies: Seq[ModuleID] = ???
  lazy val plugins: Seq[Plugins] = Seq(play.sbt.PlayScala)
  lazy val playSettings: Seq[Setting[_]] = Seq.empty
  lazy val scoverageSettings = {
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

  lazy val microservice = Project(appName, file("."))
    .enablePlugins(Seq(play.sbt.PlayScala) ++ plugins: _*)
    .settings(playSettings: _*)
    //.settings(scalaSettings: _*)
    .settings(scoverageSettings: _*)
    //.settings(publishingSettings: _*)
    //.settings(defaultSettings(): _*)
    .settings(
      //targetJvm := "jvm-1.8",
      scalaVersion := "2.11.11",
      libraryDependencies ++= appDependencies,
      parallelExecution in Test := false,
      fork in Test := false,
      retrieveManaged := true,
      evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
      routesGenerator := StaticRoutesGenerator
    )
    .configs(IntegrationTest)
    .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
    .settings(
      Keys.fork in IntegrationTest := false,
      unmanagedSourceDirectories in IntegrationTest <<= (baseDirectory in IntegrationTest) (base => Seq(base / "it")),
      //addTestReportOption(IntegrationTest, "int-test-reports"),
      testGrouping in IntegrationTest := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
      parallelExecution in IntegrationTest := false)
    .settings(
      resolvers += "emueller-bintray" at "http://dl.bintray.com/emueller/maven",
      resolvers += Resolver.jcenterRepo,
      resolvers += "spray repo" at "http://repo.spray.io")

  val appName: String
}

private object TestPhases {

  def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
    tests map {
      test => new Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
    }
}