
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/davewright/development/scala/heroku/team10bot/conf/routes
// @DATE:Mon Aug 07 18:02:15 BST 2017

import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:1
package com.macroservices.controllers {

  // @LINE:4
  class ReverseStartController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:4
    def start(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "start")
    }
  
  }

  // @LINE:1
  class ReverseMoveController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:1
    def move(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "move")
    }
  
    // @LINE:2
    def lastOpponentMove(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "move")
    }
  
  }


}
