package com.macroservices.models

import play.api.libs.json.Json


case class StartRequest(opponentName: String,
                        pointsToWin: Int, //Is this is irrelevent - referee decides
                        maxRounds: Int, //Is this is irrelevent - referee decides
                        dynamiteCount: Int) {

}

object StartRequest {
  implicit val startRequestFormats = Json.format[StartRequest]
}

case class LastMoveRequest(opponentLastMove: String) {

}

object LastMoveRequest {
  implicit val lastMoveRequestFormats = Json.format[LastMoveRequest]
}
