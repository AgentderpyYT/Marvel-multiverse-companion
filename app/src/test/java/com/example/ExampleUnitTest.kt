package com.example

import com.example.data.models.AbilityScores
import com.example.data.models.CharacterProfile
import com.example.data.models.DiceRollResult
import com.example.data.models.RollOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun d616_marvelDieOne_countsAsSixInSum() {
    // A roll of [3, 1 (Marvel), 4] + 2 modifier
    val roll = DiceRollResult(
      die1 = 3,
      marvelDie = 1, // Marvel result: counts as 6!
      die2 = 4,
      modifier = 2,
      targetNumber = 15
    )

    assertEquals(6, roll.marvelDieEffectiveValue)
    assertEquals(13, roll.diceSum) // 3 + 6 + 4 = 13
    assertEquals(15, roll.total) // 13 + 2 = 15
    assertTrue(roll.isFantastic)
    assertEquals(RollOutcome.FANTASTIC_SUCCESS, roll.outcome)
  }

  @Test
  fun d616_ultimateRoll_616_detected() {
    val ultimateRoll = DiceRollResult(
      die1 = 6,
      marvelDie = 1, // Counts as 6 in 616
      die2 = 6,
      modifier = 0,
      targetNumber = 25
    )

    assertTrue(ultimateRoll.isUltimate616)
    assertEquals(RollOutcome.ULTIMATE_FANTASTIC, ultimateRoll.outcome)
  }

  @Test
  fun d616_fantasticFailure_whenUnderTN() {
    val failedFantastic = DiceRollResult(
      die1 = 2,
      marvelDie = 1,
      die2 = 2,
      modifier = 0,
      targetNumber = 20
    )

    // 2 + 6 + 2 = 10 < 20
    assertEquals(10, failedFantastic.total)
    assertTrue(failedFantastic.isFantastic)
    assertEquals(RollOutcome.FANTASTIC_FAILURE, failedFantastic.outcome)
  }

  @Test
  fun character_derivedStatsCalculations() {
    val hero = CharacterProfile(
      name = "Peter Parker",
      codename = "Spider-Man",
      rank = 3,
      abilities = AbilityScores(
        melee = 3,
        agility = 5,
        resilience = 4,
        vigilance = 3,
        ego = 2,
        logic = 3
      )
    )

    // Health = Resilience * 30
    assertEquals(120, hero.maxHealth)
    // Focus = Vigilance * 30
    assertEquals(90, hero.maxFocus)
    // Karma = Rank
    assertEquals(3, hero.maxKarma)
    // Defenses = Ability + 10
    assertEquals(13, hero.defenseMelee)
    assertEquals(15, hero.defenseAgility)
    assertEquals(14, hero.defenseResilience)
    assertEquals(13, hero.defenseVigilance)
    assertEquals(12, hero.defenseEgo)
    assertEquals(13, hero.defenseLogic)
    // Damage multiplier = Rank
    assertEquals(3, hero.damageMultiplier)
  }
}
