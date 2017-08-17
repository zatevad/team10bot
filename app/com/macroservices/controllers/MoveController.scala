package com.macroservices.controllers

import javax.inject.{Inject, Singleton}

import com.macroservices.models.LastMoveRequest
import com.macroservices.service.Analyser
import com.macroservices.utils.JSONFactory
import play.api.data.validation.ValidationError
import play.api.db.Database
import play.api.libs.json.{JsPath, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

@Singleton
class MoveController @Inject()(db: Database) extends Controller {

  val analyser: Analyser = new Analyser(db)

  //called by the referee to get our next move
  def move() = Action {
    //Determine best item to use
    val bestItem: String = analyser.getBestGuess
    analyser.saveOurMove(bestItem)
    Ok(Json.toJson(bestItem.toUpperCase))
  }

  //called by the referee to inform us of the last move made by opponent
  def lastOpponentMove() = Action.async(parse.json) {
    implicit request =>
      request.body.validate[LastMoveRequest].fold(
        (error: Seq[(JsPath, Seq[ValidationError])]) => {
          Future.successful(BadRequest(JSONFactory.generateErrorJSON(play.api.http.Status.BAD_REQUEST, Left(error))))
        },
        (result: LastMoveRequest) => {
          analyser.saveLastOpponentMove(result)
          Future.successful(Ok)
        }
      )
  }
}
