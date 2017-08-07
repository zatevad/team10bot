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

  def storeBattle(battle: Battle): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("INSERT INTO Battles VALUES ("
        + "opponentId = " + battle.opponentId + ","
        + "opponentWeapon = " + battle.opponentWeapon + ","
        + "myWeapon = " + battle.myWeapon + ","
        + "result = " + battle.result +
        ") RETURNING id")

      rs.next();
      rs.getInt(1);
    }
  }

  def storeInitialisation(opponentId: Int, startRequest: StartRequest): Int = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("INSERT INTO Wars VALUES ("
        + "opponentId = " + opponentId + ","
        + "pointsToWin = " + startRequest.pointsToWin + ","
        + "maxRounds = " + startRequest.maxRounds + ","
        + "roundsRemaining = " + startRequest.maxRounds + ","
        + "dynamiteCount = " + startRequest.dynamiteCount + ","
        + "dynamiteCountRemaining = " + startRequest.dynamiteCount +
        ") RETURNING id")

      rs.next();
      rs.getInt(1);
    }
  }

  def decrementDynamiteCount(warId: Int) = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT dynamiteCountRemaining FROM wars WHERE wars.id = " + warId)

      rs.next();
      val remainingToUpdate = rs.getInt(1) - 1;

      stmt.executeUpdate("INSERT INTO Wars VALUES ("
        + "dynamiteCountRemaining = " + remainingToUpdate +
        ")")
    }
  }

  def decrementmMaxRounds(warId: Int) = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT roundsRemaining FROM wars WHERE wars.id = " + warId)

      rs.next();
      val remainingToUpdate = rs.getInt(1) - 1;

      stmt.executeUpdate("INSERT INTO Wars VALUES ("
        + "roundsRemaining = " + remainingToUpdate +
        ")")
    }
  }

  private def storeOpponent(opponentName: String): Int = {

    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("INSERT INTO Opponents VALUES ("
        + "opponentName = " + opponentName  + ") RETURNING id")

      rs.next();
      rs.getInt(1);
    }
  }

  def storeAndOrRetrieveOpponentId(opponentName: String): Int = {
//Not good code
    val opponentIds = ListBuffer[Int]()
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT * FROM Opponents WHERE Opponents.opponentName = " + opponentName)

      while (rs.next) {
        val opponentId = rs.getInt("id")
        opponentIds += opponentId
      }
    }
    val list = opponentIds.toList
    if(list.isEmpty) {
      storeOpponent(opponentName)
      storeAndOrRetrieveOpponentId(opponentName)
    }
    else list.head
  }

  def retrieveBattlesByOpponent(opponentName: String): List[Battle] = {

    val battles = ListBuffer[Battle]()
    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT * FROM Battles, Opponents WHERE Battles.opponentId = Opponents.opponentId" +
        " AND opponentName = " + opponentName)

      while (rs.next) {
        val id = rs.getInt("id")
        val opp = rs.getString("opponentName")
        val ow = rs.getString("opponentWeapon")
        val mw = rs.getString("myWeapon")
        val res = rs.getString("result")
        val ts = new DateTime(rs.getTimestamp("created"))

        val battle = Battle(id, opp, ow, mw, res, ts)
        battles += battle
      }
    }
    battles.toList
  }

  def retrieveAllBattles(): List[Battle] = {
    val battles = ListBuffer[Battle]()

    db.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery("SELECT * FROM Battles, Opponents WHERE Battles.opponentId = Opponents.opponentId")

      while (rs.next) {
        val id = rs.getInt("id")
        val opp = rs.getString("opponentId")
        val ow = rs.getString("opponentWeapon")
        val mw = rs.getString("myWeapon")
        val res = rs.getString("result")
        val ts = new DateTime(rs.getTimestamp("created"))

        val battle = Battle(id, opp, ow, mw, res, ts)
        battles += battle
      }
    }
    battles.toList
  }

  //Table creation
  def createOpponentsTable() = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Opponents" +
        " (id integer PRIMARY KEY DEFAULT nextval('serial'), " +
        "opponentName varchar(40) NOT NULL, " +
        "created timestamp DEFAULT NOW())")
    }
  }

  def createWarsTable() = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Wars" +
        " (id integer PRIMARY KEY DEFAULT nextval('serial'), " +
        "opponentId integer NOT NULL, " +
        "pointsToWin integer" +
        "maxRounds integer" +
        "roundsRemaining integer" +
        "dynamiteCount integer" +
        "dynamiteCountRemaining integer" +
        "created timestamp DEFAULT NOW())")
    }
  }

  def createBattlesTable() = {
    db.withConnection { conn =>
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Battles" +
        " (id integer PRIMARY KEY DEFAULT nextval('serial'), " +
        "opponentId varchar(40) NOT NULL, " +
        "opponentWeapon varchar(10)" +
        "myWeapon varchar(10)" +
        "result varchar(10)" +
        "created timestamp DEFAULT NOW())")
    }
  }
}