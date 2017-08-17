package com.macroservices.controllers

import com.macroservices.database.FakeBotApplication
import com.macroservices.service.{Analyser, Storage}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.db.Database
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{status, _}

class StartControllerSpec extends PlaySpec with FakeBotApplication with MockitoSugar {

  val mockDB = mock[Database]
  val mockStorage = mock[Storage]

  val SUT = new StartController(mockDB) {
    override val analyser: Analyser = mock[Analyser]
  }

  "Start controller" must {
    "return 200 for a POST to /start" in {
      (new Storage(mockDB)).dropTables()
      when(SUT.analyser.initialise(any())).thenReturn(4)

      val json =
        """{
          | "opponentName": "Opponent 1",
          | "pointsToWin": 1000,
          | "maxRounds": 2000,
          | "dynamiteCount": 100
          | }""".stripMargin
      val request = FakeRequest(POST, "/start").withHeaders("Host" -> "localhost").withJsonBody(Json.parse(json))
      val result = route(app, request).get

      status(result) mustBe OK
    }
  }
}
