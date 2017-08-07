
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/davewright/development/scala/heroku/team10bot/conf/routes
// @DATE:Mon Aug 07 18:02:15 BST 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
