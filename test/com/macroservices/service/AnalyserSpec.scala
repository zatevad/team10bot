package com.macroservices.service

import com.macroservices.database.FakeBotApplication
import com.macroservices.models.{LastMoveRequest, StartRequest}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.db.{Database, Databases}
import org.mockito.Mockito._

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

      SUT.initialise(StartRequest("opponent 1", 100, 500, 50))

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

//    "return dynamite if count greater than 0" in {
//      SUT.storage.dropTables()
//      val warId = SUT.initialise(StartRequest("opponent 1", 100, 500, 50))
//      when(SUT.randomWeapon).thenReturn("dynamite")
//
//      val bestguess = SUT.getBestGuess
//      bestguess mustBe "dynamite"
//    }

    "not return dynamite if count less than 0" in {
      SUT.storage.dropTables()
      val warId = SUT.initialise(StartRequest("opponent 1", 100, 500, 0))
      when(SUT.randomWeapon).thenReturn("dynamite")

      val bestguess = SUT.getBestGuess
      bestguess must not be "dynamite"
    }
  }
}
