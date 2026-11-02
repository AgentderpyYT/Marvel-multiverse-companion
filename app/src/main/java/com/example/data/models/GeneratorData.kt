package com.example.data.models

import kotlin.random.Random

data class StoryGenerationResult(
  val hook: String,
  val villainScheme: String,
  val mcGuffin: String,
  val sceneSetup: String,
  val complication: String,
  val twist: String
) {
  fun toFormattedText(): String = """
    STORY PROMPT:
    • Hook: $hook
    • Villain & Scheme: $villainScheme
    • McGuffin / Objective: $mcGuffin
    • Scene Setup: $sceneSetup
    • Complication: $complication
    • Unexpected Twist: $twist
  """.trimIndent()
}

data class NpcGenerationResult(
  val name: String,
  val codename: String,
  val rank: Int,
  val archetype: String,
  val origin: String,
  val motivation: String,
  val personalityQuirk: String,
  val signaturePower: String
) {
  fun toFormattedText(): String = """
    NPC PROFILE:
    • Codename: $codename (${name})
    • Rank: Rank $rank | Archetype: $archetype | Origin: $origin
    • Motivation: $motivation
    • Personality Quirk: $personalityQuirk
    • Signature Power: $signaturePower
  """.trimIndent()
}

data class EncounterGenerationResult(
  val enemies: String,
  val terrain: String,
  val stakes: String,
  val complication: String
) {
  fun toFormattedText(): String = """
    TACTICAL ENCOUNTER:
    • Hostile Forces: $enemies
    • Battlefield & Hazards: $terrain
    • High Stakes: $stakes
    • Tactical Complication: $complication
  """.trimIndent()
}

object GeneratorTables {

  // Story Elements
  val hooks = listOf(
    "A dimensional rift tears open during a parade in Times Square.",
    "A frantic Roxxon whistleblower delivers an encrypted quantum drive to the heroes' base.",
    "A mysterious EMP blacks out three city blocks, followed by the sound of shattering vault doors.",
    "An alien satellite crash-lands in Central Park, transmitting a universal distress beacon.",
    "Former SHIELD sleeper agents are activated simultaneously across five continents.",
    "A beloved local superhero goes rogue and begins kidnapping mayoral candidates.",
    "A temporal anomaly causes prehistoric dinosaurs to stampede down Fifth Avenue.",
    "A dark ritual at the top of the Chrysler Building turns the midnight sky blood red."
  )

  val villainSchemes = listOf(
    "Doctor Doom's splinter cell is siphoning geothermal energy to breach the Negative Zone.",
    "Hydra bioweapons division plans to release a mutagenic mist into the municipal water supply.",
    "An ambitious Symbiote cult seeks a host of pure cosmic power to birth a new hive.",
    "Kingpin is laundering stolen alien technology through underground superhero fighting rings.",
    "A rogue AI from the Baxter Building is overriding the city's automated defense drones.",
    "A rogue Kree zealot is assembling a doomsday terrigen bomb inside an abandoned subway tunnel.",
    "An occult cabal is sacrificing ancient artifacts to summon a dormant Elder God.",
    "A disgruntled Stark Industries technician is distributing powered exoskeleton suits to street gangs."
  )

  val mcGuffins = listOf(
    "A fractured Cosmic Cube shard that alters reality within a 100-meter radius.",
    "The Darkhold Codex page detailing immortality through blood transmutation.",
    "A container of raw Vibranium isotopes emitting an unstable subatomic pulse.",
    "The brainwave key to Professor X's Cerebro secondary security protocol.",
    "An extraterrestrial symbiote pod incubating in cryogenic suspension.",
    "A Stark nanotechnology master overrides token capable of deactivating sentinel tech.",
    "An ancient Asgardian runic beacon that points toward a buried Valkyrie cache.",
    "A pocket-dimension prison capsule containing a warlord from Earth-1610."
  )

  val sceneSetups = listOf(
    "A swaying suspension bridge during rush-hour traffic in torrential rain.",
    "The glass catwalks of a skyscraper penthouse under active laser bombardment.",
    "An underground magnetic-levitation subway line speeding at 200 mph.",
    "A crowded comic convention floor where cosplayers and real heroes blend in.",
    "A decaying industrial shipyard with towering cranes and hazardous chemical containers.",
    "A flooded museum rotunda with priceless historical antiquities underwater.",
    "The rooftop helipad of a burning hospital with rescue helicopters circling.",
    "An abandoned subway maintenance depot wired with trip-lasers and seismic traps."
  )

  val complications = listOf(
    "Dozens of trapped civilians are livestreaming the battle on social media.",
    "The battleground is on fire and structural integrity is failing each round.",
    "A torrential thunderstorm grounds flight powers and limits visibility to 15 feet.",
    "Local police mistake the heroes for the culprits and deploy anti-vigilante drones.",
    "A dimensional feedback loop randomly swaps characters' positions at the start of each round.",
    "One of the villains is wearing a dead-man's switch detonator.",
    "A hero's close personal contact is being held hostage in the crossfire.",
    "The villain has deployed a dampening field that doubles Focus costs for powers."
  )

  val twists = listOf(
    "The villain is actually a shapeshifting Skrull sleeper agent posing as a trusted ally.",
    "The entire heist was merely a decoy; the real strike was against the Avengers Mansion.",
    "The client who hired/alerted the heroes is the mastermind pulling the strings.",
    "Defeating the villain triggers a cascade that will collapse the entire borough's power grid.",
    "The villain believes they are preventing an even more apocalyptic future.",
    "The McGuffin is sentient and chooses one of the heroes as its biological protector.",
    "A rival hero team arrives and believes the players are the invaders.",
    "The villain is an alternate universe version of one of the player characters!"
  )

  // NPC Generator
  val npcFirstNames = listOf("Marcus", "Elena", "Dante", "Chloe", "Viktor", "Zoe", "Amara", "Tariq", "Gwen", "Lucas", "Maya", "Cassian")
  val npcLastNames = listOf("Vance", "Cross", "Mercer", "Reyes", "Steele", "Sinclair", "Novak", "Chen", "Knight", "Frost", "Davenport", "Castillo")
  val codenamePrefixes = listOf("Apex", "Shadow", "Crimson", "Quantum", "Iron", "Viper", "Solar", "Storm", "Frost", "Giga", "Cyber", "Nova")
  val codenameSuffixes = listOf("Strike", "Claw", "Warden", "Surge", "Blade", "Vanguard", "Specter", "Fury", "Fang", "Titan", "Bolt", "Grip")

  val npcMotivations = listOf(
    "Seeking redemption for collateral damage caused in a past super-battle.",
    "Obsessed with proving they are superior to registered Avengers.",
    "Protecting their impoverished neighborhood when official authorities turned a blind eye.",
    "Blackmailed by a corrupt corporation holding their family's debt.",
    "Searching for a cure for their unstable and painful superhuman mutation.",
    "Driven by vengeance against the syndicate that destroyed their mentor.",
    "Desperate to acquire enough wealth to escape Earth before an alien invasion.",
    "Believes super-powered individuals are gods destined to rule over humanity."
  )

  val npcQuirks = listOf(
    "Constantly narrates their own actions in the third person like a pulp comic.",
    "Fidgets with a customized Stark holographic coin whenever nervous.",
    "Wears vintage 80s sunglasses indoors and refuses to take them off.",
    "Extremely apologetic even while delivering devastating bone-shattering punches.",
    "Refuses to use technology more advanced than a mechanical typewriter.",
    "Speaks in cryptic rhyming riddles due to exposure to an Asgardian curse.",
    "Has an obsessive craving for street cart hot dogs mid-mission.",
    "Always carries a portable instant camera to document every villain encounter."
  )

  val signaturePowers = listOf(
    "Kinetic Absorption: Stores impact force to unleash shockwaves.",
    "Phase-Shifting: Can turn intangible and walk through solid titanium.",
    "Photonic Laser Gaze: Fires precision energy blasts from their eyes.",
    "Gravity Manipulation: Increases or decreases gravitational pull in a 20ft radius.",
    "Biochemical Venom: Paralyzing touch that burns through nerve pathways.",
    "Chrono-Blink: Short-range 3-second teleportation leaves an after-image.",
    "Bio-Metallic Skin: Can harden epidermis into diamond-hard organic steel.",
    "Neural Feedback: Inflicts psychic feedback on anyone targeting them with attacks."
  )

  // Encounter Generator
  val encounterEnemies = listOf(
    "Hydra Elite Shock-Troopers (Rank 1-2) led by a Mechanized Dreadnought (Rank 4).",
    "A rampaging Symbiote Behemoth (Rank 4) spawning fast larval spawn (Rank 1).",
    "Hand Assassin Ninjas (Rank 2) striking from shadows with poisoned shurikens.",
    "Advanced Sentinel Recon Unit (Rank 5) with mutant-adaptive energy shields.",
    "AIM Bio-Engineers and heavy hazmat enforcers armed with thermal disruptors.",
    "A rogue Asgardian Frost Giant (Rank 5) wielding a glacial battleaxe.",
    "A squad of Kree Starforce Mercenaries equipped with cloaking rigs and plasma carbines.",
    "A swarm of mutated subterranean Morlocks defending their water supply from intruders."
  )

  val encounterTerrains = listOf(
    "Rooftop with water towers, skylights, satellite dishes, and a 60-story drop.",
    "Sinking cargo ship container yard with crushing containers and leaking diesel oil.",
    "High-voltage electrical substation with sparking conduits that shock anything within 5ft.",
    "Times Square during New Year's Eve: dense crowds, giant digital screens, confetti flurry.",
    "Chemical research lab with volatile reagent vats and broken pressurized gas valves.",
    "Subway tunnel tracks with a third-rail electrical hazard and trains every 3 rounds.",
    "Crashing Quinjet interior in zero-G descent over the ocean.",
    "Ancient occult cathedral with collapsing marble pillars and anti-magic glyph zones."
  )

  val encounterStakes = listOf(
    "A runaway bomb train is 3 minutes away from Grand Central Terminal.",
    "A trapped school bus is teetering on the edge of a demolished overpass.",
    "The villain is 2 rounds away from uploading a corruptive virus to the global satellite grid.",
    "The building's cooling towers are melting down, threatening toxic fallout over Manhattan.",
    "A VIP diplomat is bleeding out inside an armored limousine under heavy gunfire.",
    "The villains are loading stolen vibranium crates onto an escaping stealth jet.",
    "Mutagenic gas is filling the ventilation system of a nearby children's hospital.",
    "A psychic shockwave will wipe the memories of everyone in a 1-mile radius if not defused."
  )

  val encounterComplications = listOf(
    "Dense smoke imposes Trouble on all ranged attacks and visibility checks.",
    "Civilians panic and surge forward, blocking movement lanes across the map.",
    "A second rival villain faction arrives unexpectedly to steal the objective.",
    "Structural damage causes pieces of the ceiling to fall on random spaces each round.",
    "An electromagnetic pulse deactivates all electronic gear and suits for 1 round.",
    "A hero's powers malfunction or cause unexpected collateral damage.",
    "Snipers are positioned in surrounding high-rises firing armor-piercing rounds.",
    "The terrain is slick with freezing ice or oil, requiring Agility checks to avoid falling Prone."
  )

  fun generateStory(): StoryGenerationResult = StoryGenerationResult(
    hook = hooks.random(),
    villainScheme = villainSchemes.random(),
    mcGuffin = mcGuffins.random(),
    sceneSetup = sceneSetups.random(),
    complication = complications.random(),
    twist = twists.random()
  )

  fun generateNpc(): NpcGenerationResult {
    val fName = npcFirstNames.random()
    val lName = npcLastNames.random()
    val code = "${codenamePrefixes.random()} ${codenameSuffixes.random()}"
    val rank = weightedRank()
    val arch = STANDARD_ARCHETYPES.random()
    val orig = STANDARD_ORIGINS.random()
    return NpcGenerationResult(
      name = "$fName $lName",
      codename = code,
      rank = rank,
      archetype = arch,
      origin = orig,
      motivation = npcMotivations.random(),
      personalityQuirk = npcQuirks.random(),
      signaturePower = signaturePowers.random()
    )
  }

  fun generateEncounter(): EncounterGenerationResult = EncounterGenerationResult(
    enemies = encounterEnemies.random(),
    terrain = encounterTerrains.random(),
    stakes = encounterStakes.random(),
    complication = encounterComplications.random()
  )

  // Rank distribution weighted towards playable/common encounters (Rank 2-4 most common)
  private fun weightedRank(): Int {
    val roll = Random.nextInt(100)
    return when {
      roll < 20 -> 1
      roll < 55 -> 2
      roll < 80 -> 3
      roll < 93 -> 4
      roll < 98 -> 5
      else -> 6
    }
  }
}
