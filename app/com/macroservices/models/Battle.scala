package com.macroservices.models

import org.joda.time.DateTime

case class Battle(id: Int, opponentId: String, opponentWeapon: String, myWeapon: String, result: String, created: DateTime) {

}
