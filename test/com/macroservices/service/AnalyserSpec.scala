package com.macroservices.service

import com.macroservices.database.FakeBotApplication
import com.macroservices.models.{LastMoveRequest, StartRequest}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.db.{Database, Databases}

class AnalyserSpec extends PlaySpec
  with FakeBotApplication
  with MockitoSugar {

  implicit val db: Database = Databases(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://localhost:5432/BotTest",
    name = "BotTest"
  )

  val SUT = new Analyser(db) {
    override val storage = new Storage(db)
  }

  "Analyser" must {
    "initialise" in {
      SUT.storage.dropTables()

      val start = StartRequest("opponent 1", 100, 500, 50)
      SUT.initialise(start)

      val result = SUT.storage.retrieveLastWar()
      result mustBe 1
    }

    "save Our Move 1" in {
      //SUT.storage.dropTables()
      val res = SUT.saveOurMove("SCISSORS")
      val result = SUT.storage.retrieveLastBattle()
      result mustBe 1
    }

    "save Last Opponent Move 1" in {
      //SUT.storage.dropTables()
      val res = SUT.saveLastOpponentMove(LastMoveRequest("ROCK"))
      val result = SUT.storage.retrieveLastBattle()
      result mustBe 1
    }

    "save Our Move 2" in {
      //SUT.storage.dropTables()
      val res = SUT.saveOurMove("ROCK")
      val result = SUT.storage.retrieveLastBattle()
      result mustBe 2
    }

    "save Last Opponent Move 2" in {
      //SUT.storage.dropTables()
      val res = SUT.saveLastOpponentMove(LastMoveRequest("PAPER"))
      val result = SUT.storage.retrieveLastBattle()
      result mustBe 2
    }

    "save Our Move 3" in {
      //SUT.storage.dropTables()
      val res = SUT.saveOurMove("PAPER")
      val result = SUT.storage.retrieveLastBattle()
      result mustBe 3
    }

    "save Last Opponent Move 3" in {
      //SUT.storage.dropTables()
      val res = SUT.saveLastOpponentMove(LastMoveRequest("PAPER"))
      val result = SUT.storage.retrieveLastBattle()
      result mustBe 3
    }
  }
}
