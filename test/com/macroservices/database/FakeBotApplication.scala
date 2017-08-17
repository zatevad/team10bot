package com.macroservices.database

import org.scalatest.{BeforeAndAfter, TestSuite}
import org.scalatestplus.play.OneAppPerSuite
import play.api.Application
import play.api.db.Database
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest

trait FakeBotApplication extends OneAppPerSuite with BeforeAndAfter {
  this: TestSuite =>

  val config: Map[String, _] = Map(
    "db.default.url" -> sys.env.getOrElse("DB_TEST_URL", "jdbc:postgresql://localhost:5432/BotTest")
  )
  implicit override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(config)
    .build()

  implicit val request = FakeRequest()

  before {
    val db = app.injector.instanceOf[Database]
  }


  //  def withBotTest[T](block: Database => T) = {
  //    Databases.withDatabase(
  //      driver = "org.postgresql.Driver",
  //      url = "jdbc:postgresql://localhost:5432/BotTest",
  //      name = "BotTest"
  //    )(block)
  //  }
  //
  //  implicit val database: Database = Databases(
  //    driver = "org.postgresql.Driver",
  //    url = "jdbc:postgresql://localhost:5432/BotTest",
  //    name = "BotTest"
  //  )
  //
  //  def withH2Database[T](block: Database => T) = {
  //    Databases.withInMemory(
  //      name = "h2database",
  //      urlOptions = Map(
  //        "MODE" -> "PostgresSQL"
  //      ),
  //      config = Map(
  //        "logStatements" -> true
  //      )
  //    )(block)
  //  }
  //  val TestDb: Database = Databases.inMemory(name="test")
  //
  //  implicit override lazy val app: Application = new GuiceApplicationBuilder()
  //    .configure("db.default.url" -> sys.env.getOrElse("DB_TEST_URL", "jdbc:postgresql://localhost:5432/BotTest"))
  //
  //    //.overrides(bind[Database].to[TestDb])
  //    .build()
  //
  //  implicit val db: Database = Databases(
  //    driver = "org.postgresql.Driver",
  //    url = "jdbc:postgresql://localhost:5432/BotTest",
  //    name = "BotTest"
  //  )
  //  implicit val request = FakeRequest()

  //  before {
  //    val db = app.injector.instanceOf[Database]
  //  }
}


