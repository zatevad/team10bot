package com.macroservices.utils

import play.api.Logger
import play.api.data.validation.ValidationError
import play.api.libs.json._

object JSONFactory extends JSONFactory

trait JSONFactory {

  def generateErrorJSON(status: Int, errors: Either[Seq[(JsPath, Seq[ValidationError])], Exception]): JsObject = {
    errors match {
      case Left(e) =>
        val errorsSequence = errorBuilder(e)
        Json.obj("status" -> status, "errors" -> errorsSequence)
      case Right(e) =>
        Json.obj("status" -> status, "error" -> s"${e.getMessage}")
    }
  }

  def errorBuilder(errors: Seq[(JsPath, Seq[ValidationError])]): JsArray = {
    errors.nonEmpty match {
      case true => {
        JsArray(
          errors.map {
            case (path, validationErrors) => Json.obj(
              "path" -> Json.toJson(path.toString()),
              "validationErrors" -> JsArray(validationErrors.map(
                validationError => Json.obj(
                  "message" -> JsString(validationError.message),
                  "args" -> JsArray(validationError.args.map(
                    _ match {
                      case x: Int => JsNumber(x)
                      case x => JsString(x.toString)
                    }
                  ))
                ))
              )
            )
          }
        )
      }
      case false =>
        Logger.warn("JSONFactory.errorBuilder - Error while generating JSON response")
        JsArray(Seq(JsString("Error while generating JSON response")))
    }
  }
}