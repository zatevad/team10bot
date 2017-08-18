package com.macroservices.service

import com.macroservices.models.{Battle, LastMoveRequest, StartRequest}
import com.macroservices.service.GameRules.Item
import org.joda.time.DateTime
import play.api.db.Database

import scala.util.Random

class Analyser(db: Database) extends RPSDW {

  val storage: Storage = new Storage(db)

  def battleResult(myweapon: String, theirWeapon: String): String = {
    val myItem = nameToItem(myweapon)
    val theirItem = nameToItem(theirWeapon)
    val res = check(myItem, theirItem)
    //println(res)
    res.toString
  }

  def initialise(initialisationObject: StartRequest): Int = {
    val opponentName = initialisationObject.opponentName

    //make sure tables exist
    storage.createAllTables()

    val warId = storage.startWar(initialisationObject)
    warId
  }

  def saveOurMove(bestItem: String): Int = {
    //Assumption that the referee is calling in order
    val lastWarId: Int = storage.retrieveLastWar()

    storage.storeOurMoveForBattle(bestItem, lastWarId)
  }

  def saveLastOpponentMove(result: LastMoveRequest): Int = {

    val lastBattle: Battle = storage.retrieveLastBattle()

    val resultString: String = battleResult(lastBattle.myWeapon, result.opponentLastMove)

    if(resultString.equals("Tie")) {
      //println("Tie")
      storage.storeTheirMoveForBattle(battleId = lastBattle.id, opponentWeapon = result.opponentLastMove, "Tie")
    }
    else {
      val winningWeapon: String = resultString.substring(4,resultString.indexOf(",")).toLowerCase
      //println(s"winningWeapon = ${winningWeapon}   myWeapon = ${lastBattle.myWeapon}      theirWeapon = ${result.opponentLastMove}")
      if(winningWeapon.equals(result.opponentLastMove)) {
        //println(s"Lost to ${winningWeapon}")
        storage.storeTheirMoveForBattle(battleId = lastBattle.id, opponentWeapon = result.opponentLastMove, "Lost" )
      }
      else {
        //println(s"Won with ${lastBattle.myWeapon}")
        storage.storeTheirMoveForBattle(battleId = lastBattle.id, opponentWeapon = result.opponentLastMove, "Won" )
      }
    }
  }

  def getBestGuess: String = {
    val decision = randomWeapon
    if (decision.equals("dynamite")) {
      val lastWarId: Int = storage.retrieveLastWar()
      if(storage.getDynamiteCount(lastWarId) < 0){
        getBestGuess
      } else {
        storage.decrementDynamiteCount(lastWarId)
      }
    }
    decision
  }

  def randomWeapon: String = {
    Random.setSeed(DateTime.now().getMillis)
    val index = Random.nextInt(nameToItem.size)
    nameToItem.keys.toIndexedSeq(index)
  }

  def warAnaysis(warId: Int) = {
    val warResults: List[Battle] = storage.retrieveBattlesByWar(warId)
    val roundstowin: Int = storage.getPointsToWin(warId)
    val numberWon = storage.getNumberWonInWar(warId)
    val numberLost = storage.getNumberLostInWar(warId)
    val numberTied = storage.getNumberTiedInWar(warId)

    println(s"Won = ${numberWon}  Lost = ${numberLost}  Tied = ${numberTied}")
    val lost = storage.getNumberLostInWar(warId)
    val won = storage.getNumberWonInWar(warId)
    if (won >= roundstowin) {
      println("I WON")
      System.exit(0)
    } else {
      println("I LOST")
      System.exit(0)
    }
  }
}