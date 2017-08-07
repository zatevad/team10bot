
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/davewright/development/scala/heroku/team10bot/conf/routes
// @DATE:Mon Aug 07 18:02:15 BST 2017

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:1
package com.macroservices.controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:4
  class ReverseStartController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:4
    def start: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "com.macroservices.controllers.StartController.start",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "start"})
        }
      """
    )
  
  }

  // @LINE:1
  class ReverseMoveController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:1
    def move: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "com.macroservices.controllers.MoveController.move",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "move"})
        }
      """
    )
  
    // @LINE:2
    def lastOpponentMove: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "com.macroservices.controllers.MoveController.lastOpponentMove",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "move"})
        }
      """
    )
  
  }


}
