package com.example.data.models

import java.util.UUID

data class AbilityScores(
  val melee: Int = 1,
  val agility: Int = 1,
  val resilience: Int = 1,
  val vigilance: Int = 1,
  val ego: Int = 1,
  val logic: Int = 1
)

data class PowerItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val powerSet: String = "Basic",
  val actionType: String = "Standard", // Standard, Reaction, Movement, Free
  val cost: Int = 0, // Focus cost
  val range: String = "Reach",
  val duration: String = "Instant",
  val description: String = ""
)

data class TraitItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val description: String = ""
)

data class EquipmentItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val type: String = "Gear",
  val effect: String = ""
)

data class CharacterProfile(
  val id: String = UUID.randomUUID().toString(),
  val name: String = "Peter Parker",
  val codename: String = "Spider-Hero",
  val rank: Int = 3, // 1 to 6
  val origin: String = "Weird Science",
  val archetype: String = "Striker",
  val abilities: AbilityScores = AbilityScores(
    melee = 2,
    agility = 4,
    resilience = 3,
    vigilance = 3,
    ego = 2,
    logic = 2
  ),
  val currentHealth: Int = 90,
  val currentFocus: Int = 90,
  val currentKarma: Int = 3,
  val conditions: List<String> = emptyList(),
  val powers: List<PowerItem> = emptyList(),
  val traits: List<TraitItem> = emptyList(),
  val tags: List<String> = emptyList(),
  val equipment: List<EquipmentItem> = emptyList(),
  val notes: String = ""
) {
  // Marvel Multiverse RPG Derived Values:
  val maxHealth: Int
    get() = (abilities.resilience.coerceAtLeast(1) * 30)

  val maxFocus: Int
    get() = (abilities.vigilance.coerceAtLeast(1) * 30)

  val maxKarma: Int
    get() = rank.coerceIn(1, 6)

  val initiativeMod: Int
    get() = abilities.vigilance

  val damageMultiplier: Int
    get() = rank

  val speed: Int
    get() = 25 // standard 25ft / 5 spaces

  // Defenses = Ability score + 10
  val defenseMelee: Int get() = abilities.melee + 10
  val defenseAgility: Int get() = abilities.agility + 10
  val defenseResilience: Int get() = abilities.resilience + 10
  val defenseVigilance: Int get() = abilities.vigilance + 10
  val defenseEgo: Int get() = abilities.ego + 10
  val defenseLogic: Int get() = abilities.logic + 10

  val healthBreakdown: String
    get() = "Resilience (${abilities.resilience}) × 30 = $maxHealth HP"

  val focusBreakdown: String
    get() = "Vigilance (${abilities.vigilance}) × 30 = $maxFocus Focus"

  val damageBreakdown: String
    get() = "Rank $rank = ×$damageMultiplier Damage Multiplier (Marvel die adds Marvel roll bonus)"

  val initiativeBreakdown: String
    get() = "Vigilance modifier (+${abilities.vigilance})"
}

val STANDARD_CONDITIONS = listOf(
  "Stunned",
  "Blinded",
  "Pinned",
  "Prone",
  "Bleeding",
  "Paralyzed",
  "Deafened",
  "Shaken",
  "Slowed",
  "Demoralized",
  "Unconscious"
)

val STANDARD_ORIGINS = listOf(
  "Alien",
  "High-Tech",
  "Magic",
  "Mutant",
  "Special Training",
  "Weird Science",
  "Cybernetic",
  "Mythic"
)

val STANDARD_ARCHETYPES = listOf(
  "Striker",
  "Blaster",
  "Bruiser",
  "Infiltrator",
  "Polymath",
  "Protector"
)
