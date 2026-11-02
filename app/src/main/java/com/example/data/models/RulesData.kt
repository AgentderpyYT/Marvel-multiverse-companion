package com.example.data.models

data class RuleEntry(
  val id: String,
  val category: String, // Core Rules, Combat, Damage & Health, Powers, Traits, Tags, Ranks, Equipment
  val title: String,
  val subtitle: String = "",
  val summary: String,
  val details: String = "",
  val tags: List<String> = emptyList()
)

object DefaultRulesData {
  const val USER_SUPPLIED_NOTE =
    "Community Reference Notice: This reference system provides standard rules summaries and editable placeholders designed for user-supplied content. Fill in custom rules or copy from your own books using the JSON editor below."

  fun getDefaultRules(): List<RuleEntry> = listOf(
    RuleEntry(
      id = "d616-system",
      category = "Core Rules & d616",
      title = "The d616 System",
      subtitle = "Action Checks & Die Mechanics",
      summary = "Whenever an action's outcome is uncertain, roll 3d6: two standard dice plus one Marvel die. The Marvel die's '1' counts as a 6 and triggers Fantastic results.",
      details = "Roll 3d6 (2 standard d6 + 1 Marvel die). If the Marvel die rolls a 1 (the Marvel logo), it counts as a 6 when calculating your total, and it becomes a Fantastic roll.\n\nTotal = Die 1 + Marvel Die (1 counts as 6) + Die 2 + Ability Modifier vs Target Number (TN).\n\nIf the total is 6-1-6, you rolled an Ultimate Fantastic Roll, the most legendary result possible in the multiverse!",
      tags = listOf("d616", "dice", "marvel die", "action check")
    ),
    RuleEntry(
      id = "edge-trouble",
      category = "Core Rules & d616",
      title = "Edge and Trouble",
      subtitle = "Advantage & Disadvantage Mechanics",
      summary = "Edge lets you reroll one die of your choice; Trouble forces you to reroll the best die or Marvel die.",
      details = "Edge: When circumstances favor you, you gain Edge. You may reroll any one of your three dice and take the new result.\n\nTrouble: When circumstances hinder you, you suffer Trouble. You must reroll one die (usually the Marvel die or the highest die) and keep the lower result.\n\nIf you have both Edge and Trouble on the same check, they cancel each other out.",
      tags = listOf("edge", "trouble", "reroll")
    ),
    RuleEntry(
      id = "fantastic-results",
      category = "Core Rules & d616",
      title = "Fantastic Rolls",
      subtitle = "Fantastic Success & Fantastic Failure",
      summary = "A 1 on the Marvel Die triggers a Fantastic roll regardless of total, altering the narrative and multiplying damage.",
      details = "Fantastic Success: If the Marvel die is 1 and your Total >= Target Number, you achieve a Fantastic Success. You deal extra damage (often multiplying by your Rank or adding Marvel die value), trigger bonus power effects, and gain tactical momentum.\n\nFantastic Failure: If the Marvel die is 1 but your Total < Target Number, you fail the intended task, but a dramatic superhero twist occurs in your favor or advances the story positively.",
      tags = listOf("fantastic success", "critical", "fantastic failure")
    ),
    RuleEntry(
      id = "combat-actions",
      category = "Combat & Actions",
      title = "Action Economy",
      subtitle = "Standard, Movement, and Reactions",
      summary = "Each round on your turn, you get 1 Standard Action, 1 Movement, and up to 1 Reaction per round.",
      details = "1. Standard Action: Make an attack, activate a standard power, assist an ally, administer first aid, or interact with complex machinery.\n2. Movement: Move up to your Run Speed (standard 25 ft / 5 spaces), or use flight/climbing.\n3. Reaction: Triggered outside your turn, such as dodging, blocking, opportunity strikes, or defensive power triggers.\n4. Free Action: Speak briefly, drop an item, or perform trivial gestures.",
      tags = listOf("combat", "actions", "standard", "reaction", "movement")
    ),
    RuleEntry(
      id = "damage-health-focus",
      category = "Damage & Health",
      title = "Health and Focus Pools",
      subtitle = "Physical and Mental/Spiritual Reserves",
      summary = "Health measures physical resilience against bodily injury; Focus powers special abilities and resists mental assaults.",
      details = "Health: Derived from Resilience x 30. Reduced by physical, energy, and elemental attacks. When reduced to 0, the character is Unconscious or dying.\n\nFocus: Derived from Vigilance x 30. Spent to activate powers and reduced by psychic or demoralizing attacks. At 0 Focus, the character is Demoralized and cannot spend Focus on powers.\n\nKarma: Spent to reroll dice, reduce damage, or bolster allies.",
      tags = listOf("health", "focus", "karma", "damage", "unconscious")
    ),
    RuleEntry(
      id = "ranks-guide",
      category = "Ranks & Scale",
      title = "Rank Progression (1 to 6)",
      subtitle = "Power tiers from Street Level to Multiversal",
      summary = "Ranks determine damage multipliers, power limits, and karma allowance from Rank 1 (street) to Rank 6 (cosmic).",
      details = "Rank 1: Street Level (Rookie vigilantes, beat cops, private eyes)\nRank 2: Neighborhood Level (Spider-Man early years, Daredevil, Luke Cage)\nRank 3: Urban / Regional (Spider-Man seasoned, Wolverine, Cyclops)\nRank 4: Champion / National (Iron Man, Captain America, Storm)\nRank 5: Cosmic / Planetary (Thor, Hulk, Doctor Strange, Silver Surfer)\nRank 6: Multiversal / Reality-Warping (Thanos with Infinity Gauntlet, Galactus, Scarlet Witch)",
      tags = listOf("ranks", "power level", "scale")
    ),
    RuleEntry(
      id = "archetypes-ref",
      category = "Archetypes",
      title = "Character Archetypes",
      subtitle = "Striker, Blaster, Bruiser, Infiltrator, Polymath, Protector",
      summary = "Archetypes define combat roles, primary abilities, and bonus modifiers for superhero teams.",
      details = "Striker: High Melee damage and mobility (e.g., Wolverine, Spider-Man).\nBlaster: Ranged energy attacks, Agility/Ego focus (e.g., Cyclops, Human Torch).\nBruiser: Extreme Resilience and Melee defense, high HP (e.g., Hulk, Thing).\nInfiltrator: Stealth, speed, Agility, and precision attacks (e.g., Black Widow, Nightcrawler).\nPolymath: Versatile jack-of-all-trades with Logic/Vigilance (e.g., Iron Man, Mister Fantastic).\nProtector: Defense-oriented, shields allies and draws aggro (e.g., Captain America, Colossus).",
      tags = listOf("archetype", "striker", "blaster", "bruiser", "polymath")
    ),
    RuleEntry(
      id = "power-sets-ref",
      category = "Powers",
      title = "Power Sets Structure",
      subtitle = "Thematic categories of superhuman abilities",
      summary = "Powers are grouped into thematic sets requiring Focus to activate, such as Martial Arts, Super-Strength, Energy Control, and Telepathy.",
      details = "[User Content Placeholder]\nAdd your power write-ups here. Common official sets include:\n- Martial Arts & Melee Weapons\n- Super-Strength & Brawling\n- Energy Control & Projection\n- Telepathy & Mental Domination\n- Telekinesis & Force Fields\n- Phasing & Size Alteration\n- Elemental Control (Fire, Ice, Lightning, Earth)\n- Tactics & Command\n\nUse the JSON editor below to insert your own customized power library entries.",
      tags = listOf("powers", "power sets", "focus")
    ),
    RuleEntry(
      id = "traits-tags-ref",
      category = "Traits",
      title = "Traits and Tags Overview",
      subtitle = "Distinctive talents and roleplay descriptors",
      summary = "Traits give mechanical bonuses or special rules exceptions; Tags describe background, identity, and story affiliations.",
      details = "Traits:\nProvide permanent passive perks or special situational abilities (e.g., Combat Reflexes, Famous, Quick Reflexes, Connections, Surprising, Tech Reliance).\n\nTags:\nDo not grant direct stat bonuses, but define legal rights, affiliations, and story vulnerabilities (e.g., Heroic, Secret Identity, Avengers, Mutant, Public Identity, Lab Access).",
      tags = listOf("traits", "tags", "perks", "affiliations")
    ),
    RuleEntry(
      id = "equipment-ref",
      category = "Equipment",
      title = "Gear, Armor, and Gadgets",
      subtitle = "Heroic equipment, weapons, and vehicles",
      summary = "Equipment grants armor protection, weapons damage modifiers, or specialized movement capabilities.",
      details = "[User Content Placeholder]\nStandard gear includes:\n- Kevlar / Vibranium Body Armor (Damage reduction)\n- Web-Shooters / Grappling lines (Snaring and swinging movement)\n- Communicators / Avengers Identicards\n- Utility Belts & Smoke Pellets\n- Customized high-tech vehicles and quinjets\n\nCustomize this section with stats from your sourcebook using the JSON editor.",
      tags = listOf("equipment", "weapons", "armor", "vehicles")
    )
  )
}
