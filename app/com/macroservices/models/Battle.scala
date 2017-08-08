package com.macroservices.models

import org.joda.time.{DateTime}

case class Battle(id: Int, warId: Int, opponentWeapon: String, myWeapon: String, result: String, created: Option[DateTime] = None) {

}
