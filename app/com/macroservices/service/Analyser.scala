package com.macroservices.service

import com.macroservices.models.{LastMoveRequest, StartRequest}
import org.joda.time.DateTime
import play.api.db.Database

import scala.util.Random

class Analyser(db: Database) {

  val storage: Storage = new Storage(db)
  private val nameToItem: Map[String, Item] = Map(
    "rock" -> Rock,
    "paper" -> Paper,
    "scissors" -> Scissors,
    "dynamite" -> Dynamite,
    "waterbomb" -> WaterBomb)

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
    if (bestItem.equals("dynamite")) storage.decrementDynamiteCount(lastWarId)

    storage.storeOurMoveForBattle(bestItem, lastWarId)
  }

  def saveLastOpponentMove(result: LastMoveRequest): Int = {

    val lastBattleId: Int = storage.retrieveLastBattle()
    storage.storeTheirMoveForBattle(opponentWeapon = result.opponentLastMove, battleId = lastBattleId)
  }

  def getBestGuess: String = {
    val decision = randomWeapon
    decision
  }

  private def randomWeapon: String = {
    Random.setSeed(DateTime.now().getMillis)
    val index = Random.nextInt(nameToItem.size)
    nameToItem.keys.toIndexedSeq(index)
  }

}

trait Item

case object Rock extends Item

case object Paper extends Item

case object Scissors extends Item

case object Dynamite extends Item

case object WaterBomb extends Item