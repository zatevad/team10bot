package com.macroservices.controllers

import javax.inject.{Inject, Singleton}

import com.macroservices.models.StartRequest
import com.macroservices.service.Analyser
import com.macroservices.utils.JSONFactory
import play.api.db.Database
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

@Singleton
class StartController @Inject()(db: Database) extends Controller {

  val analyser:Analyser = new Analyser(db)

  def start() = Action.async(parse.json) {
    implicit request =>
      request.body.validate[StartRequest].fold(
        error => {
          println(error)
          Future.successful(BadRequest(JSONFactory.generateErrorJSON(play.api.http.Status.BAD_REQUEST, Left(error))))
        },
        result => {
          println(s"StartController/start = ${result}")
          analyser.initialise(result)
          Future.successful(Ok)
        }
      )
  }
}
