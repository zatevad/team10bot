package com.macroservices.service

import javax.inject.Inject

import com.macroservices.models.{Battle, LastMoveRequest, StartRequest}
import org.joda.time.{DateTime, LocalDate}
import play.api.db._

import scala.util.Random

class Analyser @Inject()(db: Database){

  val storage: Storage = new Storage(db)

  def initialise(initialisationObject: StartRequest): Unit = {
//    val dynamiteCount = initialisationObject.dynamiteCount
    val opponentName = initialisationObject.opponentName
//    val pointsToWin = initialisationObject.pointsToWin
//    val maxRounds = initialisationObject.maxRounds
    //make sure tables exist
    storage.createOpponentsTable
    storage.createBattlesTable
    storage.createWarsTable()

    //save initialisation data
    //see if we've played before
    val opponentId = storage.storeAndOrRetrieveOpponentId(opponentName)
    storage.storeInitialisation(opponentId, initialisationObject)

  }

  def saveOurMove(bestItem: String, warId: Int): Unit = {

    if(bestItem.equals("dynamite")) storage.decrementDynamiteCount(warId)
    //How do we keep in sync with last opponent move

    val battleId: Int = storage.storeOurMoveForBattle(bestItem, warId)
  }

  def saveLastOpponentMove(result: LastMoveRequest, battleId: Int): Unit = {
//update their last move
    storage.storeTheirMoveForBattle(opponentWeapon = result.opponentLastMove,
      battleId = battleId)
  }

  def getBestGuess: String = {
    val decision = randomWeapon
    println(s"Analyser = ${decision}")
    decision
  }

  private def randomWeapon: String = {
    Random.setSeed(DateTime.now().getMillis)
    val index = Random.nextInt(nameToItem.size)
    nameToItem.keys.toIndexedSeq(index)
  }

  private val nameToItem: Map[String, Item] = Map(
    "rock" -> Rock,
    "paper" -> Paper,
    "scissors" -> Scissors,
    "dynamite" -> Dynamite,
    "waterbomb" -> WaterBomb)

}

trait Item

case object Rock extends Item

case object Paper extends Item

case object Scissors extends Item

case object Dynamite extends Item

case object WaterBomb extends Item