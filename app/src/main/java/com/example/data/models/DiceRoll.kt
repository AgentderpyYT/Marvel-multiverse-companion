package com.example.data.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class EdgeTroubleMode {
  NONE,
  EDGE,
  TROUBLE
}

enum class RollOutcome(val title: String, val badgeColorHex: Long) {
  ULTIMATE_FANTASTIC("ULTIMATE FANTASTIC ROLL (6-1-6)!", 0xFFFFD700),
  FANTASTIC_SUCCESS("FANTASTIC SUCCESS (MARVEL ROLL)!", 0xFF10B981),
  FANTASTIC_FAILURE("FANTASTIC FAILURE (DRAMATIC TWIST)!", 0xFFF59E0B),
  SUCCESS("SUCCESS", 0xFF00E5FF),
  FAILURE("FAILURE", 0xFFEF4444),
  CHECK("CHECK RESULT", 0xFF9CA3AF)
}

data class DiceRollResult(
  val id: String = System.currentTimeMillis().toString(),
  val die1: Int,
  val marvelDie: Int, // 1 to 6. A roll of 1 is the Marvel 'M' result!
  val die2: Int,
  val modifier: Int = 0,
  val targetNumber: Int? = null,
  val label: String = "Action Check",
  val timestamp: Long = System.currentTimeMillis(),
  val edgeTroubleMode: EdgeTroubleMode = EdgeTroubleMode.NONE,
  val rerolledDieIndex: Int? = null,
  val originalDice: List<Int>? = null
) {
  // In the d616 system, a 1 on the Marvel Die counts as 6 in the numerical sum!
  val marvelDieEffectiveValue: Int
    get() = if (marvelDie == 1) 6 else marvelDie

  val diceSum: Int
    get() = die1 + marvelDieEffectiveValue + die2

  val total: Int
    get() = diceSum + modifier

  val isFantastic: Boolean
    get() = marvelDie == 1

  val isUltimate616: Boolean
    get() = die1 == 6 && marvelDie == 1 && die2 == 6

  val outcome: RollOutcome
    get() {
      if (isUltimate616) return RollOutcome.ULTIMATE_FANTASTIC
      val tn = targetNumber
      return if (tn != null) {
        if (total >= tn) {
          if (isFantastic) RollOutcome.FANTASTIC_SUCCESS else RollOutcome.SUCCESS
        } else {
          if (isFantastic) RollOutcome.FANTASTIC_FAILURE else RollOutcome.FAILURE
        }
      } else {
        if (isFantastic) RollOutcome.FANTASTIC_SUCCESS else RollOutcome.CHECK
      }
    }

  val formattedTime: String
    get() {
      val sdf = SimpleDateFormat("h:mm:ss a", Locale.getDefault())
      return sdf.format(Date(timestamp))
    }

  val mathBreakdown: String
    get() {
      val mStr = if (marvelDie == 1) "M(6)" else "$marvelDie"
      val modStr = if (modifier >= 0) "+$modifier" else "$modifier"
      val tnStr = if (targetNumber != null) " vs TN $targetNumber" else ""
      return "[$die1 + $mStr + $die2] $modStr = $total$tnStr"
    }
}
