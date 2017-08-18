import com.macroservices.models.{LastMoveRequest, StartRequest}
import com.macroservices.service.Analyser
import play.api.db.{Database, Databases}



object Main {

  implicit val db: Database = Databases(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://localhost:5432/BotTest",
    name = "BotTest"
  )

  def main(args: Array[String]): Unit = {
    // $COVERAGE-OFF$ Disabling coverage for main method since it is a simple test driver
    if (args.length == 0 || args.length > 2) {
      println(
        """
          |Hello Human. Welcome to Rock-Paper-Scissors-Dynamite-Waterbomb!
          |
          |If you want to run a computer vs computer game, just pass "computer" as a command-line argument
          |If you want to play against the computer, pass in your selection (rock, paper or scissors) as a
          |command-line argument
          |
          |For example:
          |
          |$ java -jar davebot.jar computer
          |$ java -jar davebot.jar paper
          |
          |You can also run the game as a REST service. To do this, pass "web" as a command-line argument:
          |
          |$ java -jar davebot.jar web
          |
          |The service will run on port 8080.
          |
          |Enjoy!
        """.stripMargin)
    } else {
      val analyser = new Analyser(db)
      analyser.storage.dropTables()

      val numGames: Int = 2000
      val newOpponent:String = "Opponent 1"
      val numBattlesToWin: Int = 1000

      val warId: Int = analyser.initialise(StartRequest(newOpponent, numBattlesToWin, numGames, 100))

      println(s"new opponent = ${newOpponent}  warId is ${warId}")

      var x = 0
      for(x <- 1 to numGames )
      {
        //move
        val bestItem: String = analyser.getBestGuess
        val battleId = analyser.saveOurMove(bestItem)

        //opponent move
        analyser.saveLastOpponentMove(LastMoveRequest(analyser.getBestGuess))
        println(s"war Id = ${warId}  battleId = ${battleId}" )

      }
      analyser.warAnaysis(warId)
    }
  }
}