package com.example.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.data.models.AbilityScores
import com.example.data.models.CharacterProfile
import com.example.data.models.DefaultRulesData
import com.example.data.models.DiceRollResult
import com.example.data.models.EquipmentItem
import com.example.data.models.PowerItem
import com.example.data.models.RuleEntry
import com.example.data.models.SessionEntryType
import com.example.data.models.SessionLogEntry
import com.example.data.models.TraitItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class CompanionRepository(private val context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("marvel_d616_companion_prefs", Context.MODE_PRIVATE)

  private val _characters = MutableStateFlow<List<CharacterProfile>>(emptyList())
  val characters: StateFlow<List<CharacterProfile>> = _characters.asStateFlow()

  private val _activeCharacterId = MutableStateFlow<String?>(null)
  val activeCharacterId: StateFlow<String?> = _activeCharacterId.asStateFlow()

  private val _rollHistory = MutableStateFlow<List<DiceRollResult>>(emptyList())
  val rollHistory: StateFlow<List<DiceRollResult>> = _rollHistory.asStateFlow()

  private val _rules = MutableStateFlow<List<RuleEntry>>(emptyList())
  val rules: StateFlow<List<RuleEntry>> = _rules.asStateFlow()

  private val _sessionLogs = MutableStateFlow<List<SessionLogEntry>>(emptyList())
  val sessionLogs: StateFlow<List<SessionLogEntry>> = _sessionLogs.asStateFlow()

  init {
    loadAll()
  }

  fun getActiveCharacter(): CharacterProfile? {
    val id = _activeCharacterId.value ?: return _characters.value.firstOrNull()
    return _characters.value.find { it.id == id } ?: _characters.value.firstOrNull()
  }

  fun setActiveCharacter(id: String) {
    _activeCharacterId.value = id
    prefs.edit().putString("active_char_id", id).apply()
  }

  // --- Character operations ---

  fun saveCharacter(character: CharacterProfile) {
    val list = _characters.value.toMutableList()
    val index = list.indexOfFirst { it.id == character.id }
    if (index >= 0) {
      list[index] = character
    } else {
      list.add(0, character)
    }
    _characters.value = list
    if (_activeCharacterId.value == null) {
      _activeCharacterId.value = character.id
    }
    persistCharacters()
  }

  fun duplicateCharacter(character: CharacterProfile): CharacterProfile {
    val copy = character.copy(
      id = UUID.randomUUID().toString(),
      name = "${character.name} (Copy)",
      codename = "${character.codename} (Alt)"
    )
    saveCharacter(copy)
    return copy
  }

  fun deleteCharacter(id: String) {
    val list = _characters.value.filter { it.id != id }
    _characters.value = list
    if (_activeCharacterId.value == id) {
      _activeCharacterId.value = list.firstOrNull()?.id
      prefs.edit().putString("active_char_id", _activeCharacterId.value).apply()
    }
    persistCharacters()
  }

  fun adjustHealth(characterId: String, delta: Int) {
    val char = _characters.value.find { it.id == characterId } ?: return
    val newHp = (char.currentHealth + delta).coerceIn(0, char.maxHealth)
    saveCharacter(char.copy(currentHealth = newHp))
  }

  fun adjustFocus(characterId: String, delta: Int) {
    val char = _characters.value.find { it.id == characterId } ?: return
    val newFocus = (char.currentFocus + delta).coerceIn(0, char.maxFocus)
    saveCharacter(char.copy(currentFocus = newFocus))
  }

  fun adjustKarma(characterId: String, delta: Int) {
    val char = _characters.value.find { it.id == characterId } ?: return
    val newKarma = (char.currentKarma + delta).coerceIn(0, char.maxKarma + 10)
    saveCharacter(char.copy(currentKarma = newKarma))
  }

  fun toggleCondition(characterId: String, condition: String) {
    val char = _characters.value.find { it.id == characterId } ?: return
    val current = char.conditions.toMutableList()
    if (current.contains(condition)) {
      current.remove(condition)
    } else {
      current.add(condition)
    }
    saveCharacter(char.copy(conditions = current))
  }

  // --- Dice Roll History ---

  fun addRollResult(result: DiceRollResult) {
    val list = _rollHistory.value.toMutableList()
    list.add(0, result)
    if (list.size > 50) {
      list.removeAt(list.lastIndex)
    }
    _rollHistory.value = list
    persistRolls()
  }

  fun clearRollHistory() {
    _rollHistory.value = emptyList()
    prefs.edit().remove("roll_history_json").apply()
  }

  // --- Rules operations ---

  fun saveRule(rule: RuleEntry) {
    val list = _rules.value.toMutableList()
    val idx = list.indexOfFirst { it.id == rule.id }
    if (idx >= 0) {
      list[idx] = rule
    } else {
      list.add(0, rule)
    }
    _rules.value = list
    persistRules()
  }

  fun deleteRule(id: String) {
    _rules.value = _rules.value.filter { it.id != id }
    persistRules()
  }

  fun resetRulesToDefault() {
    _rules.value = DefaultRulesData.getDefaultRules()
    persistRules()
  }

  // --- Session Log operations ---

  fun addSessionLog(title: String, content: String, type: SessionEntryType = SessionEntryType.NOTE, metadata: String? = null) {
    val entry = SessionLogEntry(
      title = title,
      content = content,
      type = type,
      metadata = metadata
    )
    val list = _sessionLogs.value.toMutableList()
    list.add(0, entry)
    _sessionLogs.value = list
    persistLogs()
  }

  fun deleteSessionLog(id: String) {
    _sessionLogs.value = _sessionLogs.value.filter { it.id != id }
    persistLogs()
  }

  fun clearSessionLogs() {
    _sessionLogs.value = emptyList()
    prefs.edit().remove("session_logs_json").apply()
  }

  // --- Backup & Restore (JSON Export / Import) ---

  fun exportAllDataAsJson(): String {
    val root = JSONObject()

    // Characters
    val charArray = JSONArray()
    for (c in _characters.value) {
      charArray.put(characterToJson(c))
    }
    root.put("characters", charArray)

    // Rules
    val rulesArray = JSONArray()
    for (r in _rules.value) {
      val rObj = JSONObject()
      rObj.put("id", r.id)
      rObj.put("category", r.category)
      rObj.put("title", r.title)
      rObj.put("subtitle", r.subtitle)
      rObj.put("summary", r.summary)
      rObj.put("details", r.details)
      val tagsArr = JSONArray()
      r.tags.forEach { tagsArr.put(it) }
      rObj.put("tags", tagsArr)
      rulesArray.put(rObj)
    }
    root.put("rules", rulesArray)

    // Logs
    val logsArray = JSONArray()
    for (l in _sessionLogs.value) {
      val lObj = JSONObject()
      lObj.put("id", l.id)
      lObj.put("timestamp", l.timestamp)
      lObj.put("title", l.title)
      lObj.put("content", l.content)
      lObj.put("type", l.type.name)
      lObj.put("metadata", l.metadata ?: "")
      logsArray.put(lObj)
    }
    root.put("sessionLogs", logsArray)

    root.put("exportedAt", System.currentTimeMillis())
    root.put("app", "Marvel Multiverse d616 Companion")

    return root.toString(2)
  }

  fun importAllDataFromJson(jsonStr: String): Boolean {
    return try {
      val root = JSONObject(jsonStr)

      // Characters
      if (root.has("characters")) {
        val charArray = root.getJSONArray("characters")
        val parsedChars = mutableListOf<CharacterProfile>()
        for (i in 0 until charArray.length()) {
          val obj = charArray.getJSONObject(i)
          parsedChars.add(jsonToCharacter(obj))
        }
        if (parsedChars.isNotEmpty()) {
          _characters.value = parsedChars
          _activeCharacterId.value = parsedChars.first().id
          persistCharacters()
        }
      }

      // Rules
      if (root.has("rules")) {
        val rulesArr = root.getJSONArray("rules")
        val parsedRules = mutableListOf<RuleEntry>()
        for (i in 0 until rulesArr.length()) {
          val obj = rulesArr.getJSONObject(i)
          val tagsList = mutableListOf<String>()
          if (obj.has("tags")) {
            val tArr = obj.getJSONArray("tags")
            for (j in 0 until tArr.length()) tagsList.add(tArr.getString(j))
          }
          parsedRules.add(
            RuleEntry(
              id = obj.optString("id", UUID.randomUUID().toString()),
              category = obj.optString("category", "Core Rules"),
              title = obj.optString("title", "Rule"),
              subtitle = obj.optString("subtitle", ""),
              summary = obj.optString("summary", ""),
              details = obj.optString("details", ""),
              tags = tagsList
            )
          )
        }
        if (parsedRules.isNotEmpty()) {
          _rules.value = parsedRules
          persistRules()
        }
      }

      // Logs
      if (root.has("sessionLogs")) {
        val logsArr = root.getJSONArray("sessionLogs")
        val parsedLogs = mutableListOf<SessionLogEntry>()
        for (i in 0 until logsArr.length()) {
          val obj = logsArr.getJSONObject(i)
          val typeStr = obj.optString("type", SessionEntryType.NOTE.name)
          val type = try { SessionEntryType.valueOf(typeStr) } catch (_: Exception) { SessionEntryType.NOTE }
          parsedLogs.add(
            SessionLogEntry(
              id = obj.optString("id", UUID.randomUUID().toString()),
              timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
              title = obj.optString("title", "Note"),
              content = obj.optString("content", ""),
              type = type,
              metadata = obj.optString("metadata", null)
            )
          )
        }
        _sessionLogs.value = parsedLogs
        persistLogs()
      }

      true
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  // --- Persistence helpers ---

  private fun loadAll() {
    // 1. Load characters
    val charsJson = prefs.getString("characters_json", null)
    if (charsJson.isNullOrBlank()) {
      _characters.value = createSeedCharacters()
      persistCharacters()
    } else {
      try {
        val array = JSONArray(charsJson)
        val list = mutableListOf<CharacterProfile>()
        for (i in 0 until array.length()) {
          list.add(jsonToCharacter(array.getJSONObject(i)))
        }
        _characters.value = list
      } catch (e: Exception) {
        _characters.value = createSeedCharacters()
      }
    }

    val savedActiveId = prefs.getString("active_char_id", null)
    _activeCharacterId.value = if (savedActiveId != null && _characters.value.any { it.id == savedActiveId }) {
      savedActiveId
    } else {
      _characters.value.firstOrNull()?.id
    }

    // 2. Load rules
    val rulesJson = prefs.getString("rules_json", null)
    if (rulesJson.isNullOrBlank()) {
      _rules.value = DefaultRulesData.getDefaultRules()
      persistRules()
    } else {
      try {
        val array = JSONArray(rulesJson)
        val list = mutableListOf<RuleEntry>()
        for (i in 0 until array.length()) {
          val obj = array.getJSONObject(i)
          val tags = mutableListOf<String>()
          val tArr = obj.optJSONArray("tags")
          if (tArr != null) {
            for (j in 0 until tArr.length()) tags.add(tArr.getString(j))
          }
          list.add(
            RuleEntry(
              id = obj.getString("id"),
              category = obj.getString("category"),
              title = obj.getString("title"),
              subtitle = obj.optString("subtitle", ""),
              summary = obj.getString("summary"),
              details = obj.optString("details", ""),
              tags = tags
            )
          )
        }
        _rules.value = list
      } catch (e: Exception) {
        _rules.value = DefaultRulesData.getDefaultRules()
      }
    }

    // 3. Load logs
    val logsJson = prefs.getString("session_logs_json", null)
    if (!logsJson.isNullOrBlank()) {
      try {
        val array = JSONArray(logsJson)
        val list = mutableListOf<SessionLogEntry>()
        for (i in 0 until array.length()) {
          val obj = array.getJSONObject(i)
          val type = try { SessionEntryType.valueOf(obj.optString("type")) } catch (_: Exception) { SessionEntryType.NOTE }
          list.add(
            SessionLogEntry(
              id = obj.getString("id"),
              timestamp = obj.getLong("timestamp"),
              title = obj.getString("title"),
              content = obj.getString("content"),
              type = type,
              metadata = obj.optString("metadata", null)
            )
          )
        }
        _sessionLogs.value = list
      } catch (_: Exception) {}
    } else {
      _sessionLogs.value = listOf(
        SessionLogEntry(
          title = "Campaign Kickoff: Shadows Over Manhattan",
          content = "The team convened at the rooftop overlooking Hell's Kitchen following reports of suspicious Roxxon energy shipments. Prepared action checks and calibrated focus reserves.",
          type = SessionEntryType.NOTE
        )
      )
    }

    // 4. Load rolls
    val rollsJson = prefs.getString("roll_history_json", null)
    if (!rollsJson.isNullOrBlank()) {
      try {
        val array = JSONArray(rollsJson)
        val list = mutableListOf<DiceRollResult>()
        for (i in 0 until array.length()) {
          val obj = array.getJSONObject(i)
          list.add(
            DiceRollResult(
              id = obj.getString("id"),
              die1 = obj.getInt("die1"),
              marvelDie = obj.getInt("marvelDie"),
              die2 = obj.getInt("die2"),
              modifier = obj.optInt("modifier", 0),
              targetNumber = if (obj.has("targetNumber") && !obj.isNull("targetNumber")) obj.getInt("targetNumber") else null,
              label = obj.optString("label", "Action Check"),
              timestamp = obj.optLong("timestamp", System.currentTimeMillis())
            )
          )
        }
        _rollHistory.value = list
      } catch (_: Exception) {}
    }
  }

  private fun persistCharacters() {
    val array = JSONArray()
    for (c in _characters.value) {
      array.put(characterToJson(c))
    }
    prefs.edit().putString("characters_json", array.toString()).apply()
  }

  private fun persistRules() {
    val array = JSONArray()
    for (r in _rules.value) {
      val obj = JSONObject()
      obj.put("id", r.id)
      obj.put("category", r.category)
      obj.put("title", r.title)
      obj.put("subtitle", r.subtitle)
      obj.put("summary", r.summary)
      obj.put("details", r.details)
      val tArr = JSONArray()
      r.tags.forEach { tArr.put(it) }
      obj.put("tags", tArr)
      array.put(obj)
    }
    prefs.edit().putString("rules_json", array.toString()).apply()
  }

  private fun persistLogs() {
    val array = JSONArray()
    for (l in _sessionLogs.value) {
      val obj = JSONObject()
      obj.put("id", l.id)
      obj.put("timestamp", l.timestamp)
      obj.put("title", l.title)
      obj.put("content", l.content)
      obj.put("type", l.type.name)
      obj.put("metadata", l.metadata ?: "")
      array.put(obj)
    }
    prefs.edit().putString("session_logs_json", array.toString()).apply()
  }

  private fun persistRolls() {
    val array = JSONArray()
    for (r in _rollHistory.value) {
      val obj = JSONObject()
      obj.put("id", r.id)
      obj.put("die1", r.die1)
      obj.put("marvelDie", r.marvelDie)
      obj.put("die2", r.die2)
      obj.put("modifier", r.modifier)
      if (r.targetNumber != null) obj.put("targetNumber", r.targetNumber)
      obj.put("label", r.label)
      obj.put("timestamp", r.timestamp)
      array.put(obj)
    }
    prefs.edit().putString("roll_history_json", array.toString()).apply()
  }

  private fun characterToJson(c: CharacterProfile): JSONObject {
    val obj = JSONObject()
    obj.put("id", c.id)
    obj.put("name", c.name)
    obj.put("codename", c.codename)
    obj.put("rank", c.rank)
    obj.put("origin", c.origin)
    obj.put("archetype", c.archetype)

    val abObj = JSONObject()
    abObj.put("melee", c.abilities.melee)
    abObj.put("agility", c.abilities.agility)
    abObj.put("resilience", c.abilities.resilience)
    abObj.put("vigilance", c.abilities.vigilance)
    abObj.put("ego", c.abilities.ego)
    abObj.put("logic", c.abilities.logic)
    obj.put("abilities", abObj)

    obj.put("currentHealth", c.currentHealth)
    obj.put("currentFocus", c.currentFocus)
    obj.put("currentKarma", c.currentKarma)

    val condArr = JSONArray()
    c.conditions.forEach { condArr.put(it) }
    obj.put("conditions", condArr)

    val powArr = JSONArray()
    c.powers.forEach { p ->
      val pObj = JSONObject()
      pObj.put("id", p.id)
      pObj.put("name", p.name)
      pObj.put("powerSet", p.powerSet)
      pObj.put("actionType", p.actionType)
      pObj.put("cost", p.cost)
      pObj.put("range", p.range)
      pObj.put("duration", p.duration)
      pObj.put("description", p.description)
      powArr.put(pObj)
    }
    obj.put("powers", powArr)

    val trArr = JSONArray()
    c.traits.forEach { t ->
      val tObj = JSONObject()
      tObj.put("id", t.id)
      tObj.put("name", t.name)
      tObj.put("description", t.description)
      trArr.put(tObj)
    }
    obj.put("traits", trArr)

    val tagArr = JSONArray()
    c.tags.forEach { tagArr.put(it) }
    obj.put("tags", tagArr)

    val eqArr = JSONArray()
    c.equipment.forEach { eq ->
      val eObj = JSONObject()
      eObj.put("id", eq.id)
      eObj.put("name", eq.name)
      eObj.put("type", eq.type)
      eObj.put("effect", eq.effect)
      eqArr.put(eObj)
    }
    obj.put("equipment", eqArr)

    obj.put("notes", c.notes)
    return obj
  }

  private fun jsonToCharacter(obj: JSONObject): CharacterProfile {
    val abObj = obj.optJSONObject("abilities")
    val abilities = if (abObj != null) {
      AbilityScores(
        melee = abObj.optInt("melee", 1),
        agility = abObj.optInt("agility", 1),
        resilience = abObj.optInt("resilience", 1),
        vigilance = abObj.optInt("vigilance", 1),
        ego = abObj.optInt("ego", 1),
        logic = abObj.optInt("logic", 1)
      )
    } else {
      AbilityScores()
    }

    val condList = mutableListOf<String>()
    val cArr = obj.optJSONArray("conditions")
    if (cArr != null) {
      for (i in 0 until cArr.length()) condList.add(cArr.getString(i))
    }

    val powList = mutableListOf<PowerItem>()
    val pArr = obj.optJSONArray("powers")
    if (pArr != null) {
      for (i in 0 until pArr.length()) {
        val p = pArr.getJSONObject(i)
        powList.add(
          PowerItem(
            id = p.optString("id", UUID.randomUUID().toString()),
            name = p.getString("name"),
            powerSet = p.optString("powerSet", "Basic"),
            actionType = p.optString("actionType", "Standard"),
            cost = p.optInt("cost", 0),
            range = p.optString("range", "Reach"),
            duration = p.optString("duration", "Instant"),
            description = p.optString("description", "")
          )
        )
      }
    }

    val traitList = mutableListOf<TraitItem>()
    val tArr = obj.optJSONArray("traits")
    if (tArr != null) {
      for (i in 0 until tArr.length()) {
        val t = tArr.getJSONObject(i)
        traitList.add(
          TraitItem(
            id = t.optString("id", UUID.randomUUID().toString()),
            name = t.getString("name"),
            description = t.optString("description", "")
          )
        )
      }
    }

    val tagList = mutableListOf<String>()
    val tgArr = obj.optJSONArray("tags")
    if (tgArr != null) {
      for (i in 0 until tgArr.length()) tagList.add(tgArr.getString(i))
    }

    val eqList = mutableListOf<EquipmentItem>()
    val eqArr = obj.optJSONArray("equipment")
    if (eqArr != null) {
      for (i in 0 until eqArr.length()) {
        val eq = eqArr.getJSONObject(i)
        eqList.add(
          EquipmentItem(
            id = eq.optString("id", UUID.randomUUID().toString()),
            name = eq.getString("name"),
            type = eq.optString("type", "Gear"),
            effect = eq.optString("effect", "")
          )
        )
      }
    }

    val rank = obj.optInt("rank", 3)
    val maxHp = abilities.resilience.coerceAtLeast(1) * 30
    val maxFoc = abilities.vigilance.coerceAtLeast(1) * 30

    return CharacterProfile(
      id = obj.optString("id", UUID.randomUUID().toString()),
      name = obj.optString("name", "Unknown Hero"),
      codename = obj.optString("codename", "Hero"),
      rank = rank,
      origin = obj.optString("origin", "Special Training"),
      archetype = obj.optString("archetype", "Striker"),
      abilities = abilities,
      currentHealth = obj.optInt("currentHealth", maxHp),
      currentFocus = obj.optInt("currentFocus", maxFoc),
      currentKarma = obj.optInt("currentKarma", rank),
      conditions = condList,
      powers = powList,
      traits = traitList,
      tags = tagList,
      equipment = eqList,
      notes = obj.optString("notes", "")
    )
  }

  private fun createSeedCharacters(): List<CharacterProfile> {
    return listOf(
      CharacterProfile(
        id = "spider-hero-1",
        name = "Peter Parker",
        codename = "Spider-Man",
        rank = 3,
        origin = "Weird Science",
        archetype = "Striker",
        abilities = AbilityScores(melee = 3, agility = 5, resilience = 3, vigilance = 4, ego = 2, logic = 3),
        currentHealth = 90,
        currentFocus = 120,
        currentKarma = 3,
        conditions = emptyList(),
        powers = listOf(
          PowerItem(name = "Wall-Crawling", powerSet = "Spider-Powers", actionType = "Movement", cost = 0, range = "Self", duration = "Permanent", description = "Climb solid surfaces at normal run speed."),
          PowerItem(name = "Web-Slinging", powerSet = "Spider-Powers", actionType = "Movement", cost = 5, range = "100 ft", duration = "Instant", description = "Swing through urban environments with rapid acceleration."),
          PowerItem(name = "Spider-Sense", powerSet = "Spider-Powers", actionType = "Reaction", cost = 10, range = "Self", duration = "Instant", description = "Add Edge to Agility defenses and ignore surprise attacks."),
          PowerItem(name = "Web-Trap", powerSet = "Spider-Powers", actionType = "Standard", cost = 10, range = "30 ft", duration = "1 Round", description = "Trap a target in adhesive webbing (target gains Pinned condition).")
        ),
        traits = listOf(
          TraitItem(name = "Combat Reflexes", description = "Can use 1 extra Reaction per round."),
          TraitItem(name = "Scientific Genius", description = "Edge on Logic checks involving physics and biochemistry."),
          TraitItem(name = "Surprising Agility", description = "Can leap over obstacles without movement penalties.")
        ),
        tags = listOf("Heroic", "Secret Identity", "Street Hero", "Spider-Guild", "Avengers Ally"),
        equipment = listOf(
          EquipmentItem(name = "Custom Web-Shooters", type = "Gear", effect = "Precision fluid delivery with adjustable nozzle pressure."),
          EquipmentItem(name = "Spider-Tracers", type = "Gadget", effect = "Allows tracking targets via spider-sense receiver within 5 miles.")
        ),
        notes = "With great power comes great responsibility. Dedicated defender of New York City."
      ),
      CharacterProfile(
        id = "iron-avenger-2",
        name = "Tony Stark",
        codename = "Iron Man",
        rank = 4,
        origin = "High-Tech",
        archetype = "Polymath",
        abilities = AbilityScores(melee = 2, agility = 3, resilience = 4, vigilance = 3, ego = 3, logic = 5),
        currentHealth = 120,
        currentFocus = 90,
        currentKarma = 4,
        conditions = emptyList(),
        powers = listOf(
          PowerItem(name = "Repulsor Rays", powerSet = "Armor Tech", actionType = "Standard", cost = 10, range = "60 ft", duration = "Instant", description = "Concentrated plasma blast dealing high Agility energy damage."),
          PowerItem(name = "Unibeam", powerSet = "Armor Tech", actionType = "Standard", cost = 25, range = "100 ft", duration = "Instant", description = "Chest-mounted super-charged particle beam; devastating damage."),
          PowerItem(name = "Supersonic Flight", powerSet = "Armor Tech", actionType = "Movement", cost = 5, range = "Self", duration = "Maintained", description = "Fly at Mach speeds across battlefields."),
          PowerItem(name = "Energy Shield", powerSet = "Armor Tech", actionType = "Reaction", cost = 15, range = "Self", duration = "1 Round", description = "Reinforce armor defenses by +5 against incoming attacks.")
        ),
        traits = listOf(
          TraitItem(name = "Billionaire Resources", description = "Access to state-of-the-art facilities, laboratories, and funding."),
          TraitItem(name = "Tactical Mastermind", description = "Grant allies Edge on initiative checks."),
          TraitItem(name = "Armor Reliance", description = "Abilities tied directly to functional armor status.")
        ),
        tags = listOf("Heroic", "Public Identity", "Avengers Leader", "Stark Industries", "Tech Savvy"),
        equipment = listOf(
          EquipmentItem(name = "Model 70 Armor", type = "Suit", effect = "Vibranium-titanium weave with integrated AI diagnostics."),
          EquipmentItem(name = "Stark Holographic HUD", type = "Sensors", effect = "Real-time tactical analysis and thermal scanning.")
        ),
        notes = "Genius, billionaire, playboy, philanthropist. Vanguard of technological defense."
      )
    )
  }
}
