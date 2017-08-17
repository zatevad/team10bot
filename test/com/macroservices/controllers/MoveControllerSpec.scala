package com.macroservices.controllers

import com.macroservices.database.FakeBotApplication
import com.macroservices.service.{Analyser, Storage}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.db.Database
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, _}

class MoveControllerSpec extends PlaySpec
  with FakeBotApplication
  with MockitoSugar {

  val mockDB = mock[Database]
  val mockStorage = mock[Storage]

  val SUT = new StartController(mockDB) {
    override val analyser: Analyser = mock[Analyser]
  }

  "Move controller" must {
    "return 200 for a GET" in {
      //set values into database
      val storage = new Storage(mockDB)
      storage.dropTables()
      storage.createAllTables()
      storage.storeAndOrRetrieveOpponentId("test 1")

      val request = FakeRequest(GET, "/move").withHeaders("Host" -> "localhost")
      val result = route(app, request).get

      status(result) mustBe OK
    }

    "return an acceptable value as JSON for a GET" in {
      //set values into database
      val storage = new Storage(mockDB)
      storage.dropTables()
      storage.createAllTables()
      storage.storeAndOrRetrieveOpponentId("test 1")

      val request = FakeRequest(GET, "/move").withHeaders("Host" -> "localhost")
      val result = route(app, request).get

      contentType(result) mustBe Some("application/json")

      List("\"ROCK\"",
        "\"PAPER\"",
        "\"SCISSORS\"",
        "\"DYNAMITE\"",
        "\"WATERBOMB\"").contains(contentAsString(result)) mustBe true
    }

    "return 200 for a POST" in {

      val json = """{"opponentLastMove": "PAPER"}"""
      val request = FakeRequest(POST, "/move").withHeaders("Host" -> "localhost").withJsonBody(Json.parse(json))
      val result = route(app, request).get

      status(result) mustBe OK
    }
  }
}
