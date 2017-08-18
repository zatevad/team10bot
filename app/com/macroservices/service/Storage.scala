package com.macroservices.service

import javax.inject.Inject

import com.macroservices.models.{Battle, StartRequest}
import org.joda.time.DateTime
import play.api.db._

import scala.collection.mutable.ListBuffer

class Storage @Inject()(db: Database) {
  /* This code not finished yet

  We have opponents

  A war is a complete Game against an opponent

  A Battle is an individual round

  */

  def storeOurMoveForBattle(myWeapon: String, warId: Int): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("INSERT INTO battles (warId, myWeapon) VALUES (" +
        +warId + ", '"
        + myWeapon +
        "') RETURNING id")

      rs.next();
      rs.getInt(1);
    }
  }

  def storeTheirMoveForBattle(battleId: Int, opponentWeapon: String, result: String): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeUpdate("UPDATE battles SET "
        + "opponentWeapon = '" + opponentWeapon + "', result = '" + result + "' WHERE battles.Id = " + battleId)

    }
    //return the battle Id for confimation
    battleId
  }

  def startWar(startRequest: StartRequest): Int = {
    val opponentId = storeOpponent(startRequest.opponentName)
    storeInitialisation(opponentId, startRequest)
  }

  def storeInitialisation(opponentId: Int, startRequest: StartRequest): Int = {

    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("INSERT INTO wars (opponentId, pointsToWin, maxRounds, roundsRemaining, " +
        "dynamiteCount, dynamiteCountRemaining) VALUES ("
        + opponentId + ","
        + startRequest.pointsToWin + ","
        + startRequest.maxRounds + ","
        + startRequest.maxRounds + ","
        + startRequest.dynamiteCount + ","
        + startRequest.dynamiteCount +
        ") RETURNING id")

      rs.next()
      rs.getInt(1)
    }
  }


  def retrieveLastWar(): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT * FROM wars ORDER BY id DESC LIMIT 1")

      rs.next()
      rs.getInt(1)
    }
  }

  def retrieveLastBattle(): Battle = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT * FROM battles ORDER BY id DESC LIMIT 1")

      rs.next()
        val id = rs.getInt("id")
        val war = rs.getInt("warId")
        val ow = rs.getString("opponentWeapon")
        val mw = rs.getString("myWeapon")
        val res = rs.getString("result")
        val ts = new DateTime(rs.getTimestamp("created"))

        Battle(id, war, ow, mw, res, Some(ts))

    }
  }

  //return remaining dynamite
  def decrementDynamiteCount(warId: Int): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT dynamiteCountRemaining FROM wars WHERE wars.id = " + warId)

      rs.next();
      val remainingToUpdate = rs.getInt(1) - 1;

      stmt.executeUpdate("UPDATE wars SET "
        + "dynamiteCountRemaining = " + remainingToUpdate +
        " WHERE wars.id = " + warId)
      remainingToUpdate
    }
  }

  def getDynamiteCount(warId: Int): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT dynamiteCountRemaining FROM wars WHERE wars.id = " + warId)

      rs.next();
      rs.getInt(1);
    }
  }

  def getmaxRounds(warId: Int): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT maxrounds FROM wars WHERE wars.id = " + warId)

      rs.next();
      rs.getInt(1);
    }
  }

  def getPointsToWin(warId: Int): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT pointstowin FROM wars WHERE wars.id = " + warId)

      rs.next();
      rs.getInt(1);
    }
  }

  //return remaining rounds
  def decrementmMaxRounds(warId: Int): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT roundsRemaining FROM wars WHERE wars.id = " + warId)

      rs.next();
      val remainingToUpdate = rs.getInt(1) - 1;

      stmt.executeUpdate("UPDATE wars SET "
        + "roundsRemaining = " + remainingToUpdate +
        " WHERE oars.id = " + warId)
      remainingToUpdate
    }
  }

  def storeAndOrRetrieveOpponentId(opponentName: String): Int = {

    val opponentIds = ListBuffer[Int]()
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT * FROM opponents WHERE opponents.opponentName LIKE '" + opponentName + "%'")

      while (rs.next) {
        val opponentId = rs.getInt("id")

        opponentIds += opponentId
      }
    }
    val list = opponentIds.toList
    if (list.isEmpty) {
      storeOpponent(opponentName)
    }
    else list.head
  }

  private def storeOpponent(opponentName: String): Int = {

    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("INSERT INTO opponents (opponentName) VALUES ('"
        + opponentName + "-" + uuid + "') RETURNING id")

      rs.next();
      rs.getInt(1);
    }
  }

  def uuid(): String = java.util.UUID.randomUUID.toString

  def retrieveBattlesByWar(warId: Int): List[Battle] = {

    val battles = ListBuffer[Battle]()
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT * FROM battles, wars WHERE battles.warId = " + warId)

      while (rs.next) {
        val id = rs.getInt("id")
        val war = rs.getInt("warid")
        val ow = rs.getString("opponentWeapon")
        val mw = rs.getString("myWeapon")
        val res = rs.getString("result")
        val ts = new DateTime(rs.getTimestamp("created"))

        val battle = Battle(id, war, ow, mw, res, Some(ts))
        battles += battle
      }
    }
    battles.toList
  }

  def retrieveAllBattles(): List[Battle] = {
    val battles = ListBuffer[Battle]()

    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT * FROM battles")

      while (rs.next) {
        val id = rs.getInt("id")
        val war = rs.getInt("warId")
        val ow = rs.getString("opponentWeapon")
        val mw = rs.getString("myWeapon")
        val res = rs.getString("result")
        val ts = new DateTime(rs.getTimestamp("created"))

        val battle = Battle(id, war, ow, mw, res, Some(ts))
        battles += battle
      }
    }
    battles.toList
  }

  def getNumberWonInWar(warId: Int): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT count(*) FROM battles WHERE warid = " + warId + " AND result = 'Won'")
      rs.next();
      rs.getInt(1);
    }
  }
  def getNumberLostInWar(warId: Int): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT count(*) FROM battles WHERE warid = " + warId + " AND result = 'Lost'")
      rs.next();
      rs.getInt(1);
    }
  }
  def getNumberTiedInWar(warId: Int): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT count(*) FROM battles WHERE warid = " + warId + " AND result = 'Tied'")
      rs.next();
      rs.getInt(1);
    }
  }

  //Table creation
  def createAllTables() = {
    createOpponentsTable()
    createWarsTable()
    createBattlesTable()
  }

  private def createOpponentsTable() = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS opponents " +
        "(id BIGSERIAL PRIMARY KEY , " +
        "opponentName varchar(200) NOT NULL, " +
        "created timestamp DEFAULT NOW())")
    }
  }

  private def createWarsTable() = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS wars " +
        "(id BIGSERIAL PRIMARY KEY , " +
        "opponentId bigint NOT NULL, " +
        "pointsToWin integer, " +
        "maxRounds integer, " +
        "roundsRemaining integer, " +
        "dynamiteCount integer, " +
        "dynamiteCountRemaining integer, " +
        "created timestamp DEFAULT NOW())")
    }
  }

  private def createBattlesTable() = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS battles " +
        "(id BIGSERIAL PRIMARY KEY , " +
        "warId bigint NOT NULL, " +
        "opponentWeapon varchar(10), " +
        "myWeapon varchar(10), " +
        "result varchar(10), " +
        "created timestamp DEFAULT NOW())")
    }
  }

  //utils

  //Table Clean up
  def dropTables() = {
    try {
      db.withConnection { conn =>
        val stmt = conn.createStatement

        stmt.executeUpdate("DROP table IF EXISTS battles")
        stmt.executeUpdate("DROP table IF EXISTS wars")
        stmt.executeUpdate("DROP table IF EXISTS opponents")
      }
    }
    catch {
      //do nothing
      case e: Exception => Unit
    }
  }
}