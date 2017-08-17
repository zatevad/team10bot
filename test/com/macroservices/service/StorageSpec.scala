package com.macroservices.service

import com.macroservices.database.FakeBotApplication
import com.macroservices.models.StartRequest
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.db.{Database, Databases}

class StorageSpec extends PlaySpec
  with FakeBotApplication
  with MockitoSugar {

  implicit val db: Database = Databases(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://localhost:5432/BotTest",
    name = "BotTest"
  )

  val SUT = new Storage(db)

  //keep these tests in order
  "Storage" must {
    "drop all tables if the tables don't exist" in {
      SUT.createAllTables()
      SUT.dropTables()
      val thrown1 = the[Exception] thrownBy SUT.retrieveLastWar()
      val thrown2 = the[Exception] thrownBy SUT.retrieveLastBattle()
      val thrown3 = the[Exception] thrownBy SUT.startWar(StartRequest("test 1", 2, 2, 2))

      thrown1.getMessage must include("ERROR: relation \"wars\" does not exist")
      thrown2.getMessage must include("ERROR: relation \"battles\" does not exist")
      thrown3.getMessage must include("ERROR: relation \"opponents\" does not exist")

    }

    "create all tables" in {
      SUT.createAllTables()
      noException should be thrownBy SUT.startWar(StartRequest("test 1", 2, 2, 2))
      noException should be thrownBy SUT.retrieveLastWar()
      noException should be thrownBy SUT.storeOurMoveForBattle("scissors", SUT.retrieveLastWar())
      noException should be thrownBy SUT.retrieveLastBattle()

    }

    "store Initialisation and start a war" in {
      SUT.dropTables()
      SUT.createAllTables()
      val start = StartRequest("opponent 1", 100, 500, 50)
      val result = SUT.startWar(start)
      result mustBe 1
    }
  }

}
