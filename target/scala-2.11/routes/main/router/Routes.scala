
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/davewright/development/scala/heroku/team10bot/conf/routes
// @DATE:Mon Aug 07 18:02:15 BST 2017

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:1
  MoveController_0: javax.inject.Provider[com.macroservices.controllers.MoveController],
  // @LINE:4
  StartController_1: javax.inject.Provider[com.macroservices.controllers.StartController],
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:1
    MoveController_0: javax.inject.Provider[com.macroservices.controllers.MoveController],
    // @LINE:4
    StartController_1: javax.inject.Provider[com.macroservices.controllers.StartController]
  ) = this(errorHandler, MoveController_0, StartController_1, "/")

  import ReverseRouteContext.empty

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, MoveController_0, StartController_1, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """move""", """@com.macroservices.controllers.MoveController@.move"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """move""", """@com.macroservices.controllers.MoveController@.lastOpponentMove"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """start""", """@com.macroservices.controllers.StartController@.start"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:1
  private[this] lazy val com_macroservices_controllers_MoveController_move0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("move")))
  )
  private[this] lazy val com_macroservices_controllers_MoveController_move0_invoker = createInvoker(
    MoveController_0.get.move,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "com.macroservices.controllers.MoveController",
      "move",
      Nil,
      "GET",
      """""",
      this.prefix + """move"""
    )
  )

  // @LINE:2
  private[this] lazy val com_macroservices_controllers_MoveController_lastOpponentMove1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("move")))
  )
  private[this] lazy val com_macroservices_controllers_MoveController_lastOpponentMove1_invoker = createInvoker(
    MoveController_0.get.lastOpponentMove,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "com.macroservices.controllers.MoveController",
      "lastOpponentMove",
      Nil,
      "POST",
      """""",
      this.prefix + """move"""
    )
  )

  // @LINE:4
  private[this] lazy val com_macroservices_controllers_StartController_start2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("start")))
  )
  private[this] lazy val com_macroservices_controllers_StartController_start2_invoker = createInvoker(
    StartController_1.get.start,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "com.macroservices.controllers.StartController",
      "start",
      Nil,
      "POST",
      """""",
      this.prefix + """start"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:1
    case com_macroservices_controllers_MoveController_move0_route(params) =>
      call { 
        com_macroservices_controllers_MoveController_move0_invoker.call(MoveController_0.get.move)
      }
  
    // @LINE:2
    case com_macroservices_controllers_MoveController_lastOpponentMove1_route(params) =>
      call { 
        com_macroservices_controllers_MoveController_lastOpponentMove1_invoker.call(MoveController_0.get.lastOpponentMove)
      }
  
    // @LINE:4
    case com_macroservices_controllers_StartController_start2_route(params) =>
      call { 
        com_macroservices_controllers_StartController_start2_invoker.call(StartController_1.get.start)
      }
  }
}
