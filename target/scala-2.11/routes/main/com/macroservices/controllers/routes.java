
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/davewright/development/scala/heroku/team10bot/conf/routes
// @DATE:Mon Aug 07 18:02:15 BST 2017

package com.macroservices.controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final com.macroservices.controllers.ReverseStartController StartController = new com.macroservices.controllers.ReverseStartController(RoutesPrefix.byNamePrefix());
  public static final com.macroservices.controllers.ReverseMoveController MoveController = new com.macroservices.controllers.ReverseMoveController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final com.macroservices.controllers.javascript.ReverseStartController StartController = new com.macroservices.controllers.javascript.ReverseStartController(RoutesPrefix.byNamePrefix());
    public static final com.macroservices.controllers.javascript.ReverseMoveController MoveController = new com.macroservices.controllers.javascript.ReverseMoveController(RoutesPrefix.byNamePrefix());
  }

}
