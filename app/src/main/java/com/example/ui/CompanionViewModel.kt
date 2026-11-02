package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.CharacterProfile
import com.example.data.models.DiceRollResult
import com.example.data.models.EdgeTroubleMode
import com.example.data.models.EncounterGenerationResult
import com.example.data.models.GeneratorTables
import com.example.data.models.NpcGenerationResult
import com.example.data.models.PowerItem
import com.example.data.models.RuleEntry
import com.example.data.models.SessionEntryType
import com.example.data.models.SessionLogEntry
import com.example.data.models.StoryGenerationResult
import com.example.data.models.TraitItem
import com.example.data.storage.CompanionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class CompanionViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = CompanionRepository(application.applicationContext)

  val characters: StateFlow<List<CharacterProfile>> = repository.characters
  val activeCharacterId: StateFlow<String?> = repository.activeCharacterId
  val rollHistory: StateFlow<List<DiceRollResult>> = repository.rollHistory
  val rules: StateFlow<List<RuleEntry>> = repository.rules
  val sessionLogs: StateFlow<List<SessionLogEntry>> = repository.sessionLogs

  // Dice Roller State
  private val _currentRoll = MutableStateFlow<DiceRollResult?>(null)
  val currentRoll: StateFlow<DiceRollResult?> = _currentRoll.asStateFlow()

  private val _rollModifier = MutableStateFlow(0)
  val rollModifier: StateFlow<Int> = _rollModifier.asStateFlow()

  private val _targetNumber = MutableStateFlow<Int?>(15)
  val targetNumber: StateFlow<Int?> = _targetNumber.asStateFlow()

  private val _rollLabel = MutableStateFlow("Action Check")
  val rollLabel: StateFlow<String> = _rollLabel.asStateFlow()

  private val _edgeTrouble = MutableStateFlow(EdgeTroubleMode.NONE)
  val edgeTrouble: StateFlow<EdgeTroubleMode> = _edgeTrouble.asStateFlow()

  private val _selectedDieForReroll = MutableStateFlow<Int?>(null) // 0: die1, 1: marvelDie, 2: die2
  val selectedDieForReroll: StateFlow<Int?> = _selectedDieForReroll.asStateFlow()

  private val _isRollingAnimation = MutableStateFlow(false)
  val isRollingAnimation: StateFlow<Boolean> = _isRollingAnimation.asStateFlow()

  // Generator States
  private val _currentStory = MutableStateFlow(GeneratorTables.generateStory())
  val currentStory: StateFlow<StoryGenerationResult> = _currentStory.asStateFlow()

  private val _currentNpc = MutableStateFlow(GeneratorTables.generateNpc())
  val currentNpc: StateFlow<NpcGenerationResult> = _currentNpc.asStateFlow()

  private val _currentEncounter = MutableStateFlow(GeneratorTables.generateEncounter())
  val currentEncounter: StateFlow<EncounterGenerationResult> = _currentEncounter.asStateFlow()

  init {
    // Initial standard roll
    rollDice()
  }

  fun getActiveCharacter(): CharacterProfile? = repository.getActiveCharacter()

  fun setActiveCharacter(id: String) {
    repository.setActiveCharacter(id)
  }

  // --- Dice Roller Logic ---

  fun setModifier(mod: Int) {
    _rollModifier.value = mod
  }

  fun adjustModifier(delta: Int) {
    _rollModifier.value += delta
  }

  fun setTargetNumber(tn: Int?) {
    _targetNumber.value = tn
  }

  fun setRollLabel(label: String) {
    _rollLabel.value = label
  }

  fun setEdgeTrouble(mode: EdgeTroubleMode) {
    _edgeTrouble.value = mode
  }

  fun selectDieForReroll(index: Int) {
    _selectedDieForReroll.value = if (_selectedDieForReroll.value == index) null else index
  }

  fun rollDice() {
    _isRollingAnimation.value = true
    val d1 = Random.nextInt(1, 7)
    val m = Random.nextInt(1, 7) // 1 is the Marvel 'M' result!
    val d2 = Random.nextInt(1, 7)

    val result = DiceRollResult(
      die1 = d1,
      marvelDie = m,
      die2 = d2,
      modifier = _rollModifier.value,
      targetNumber = _targetNumber.value,
      label = _rollLabel.value,
      edgeTroubleMode = _edgeTrouble.value
    )

    _currentRoll.value = result
    _selectedDieForReroll.value = null
    repository.addRollResult(result)
    _isRollingAnimation.value = false
  }

  fun rerollSelectedDie() {
    val current = _currentRoll.value ?: return
    val dieToReroll = _selectedDieForReroll.value ?: return

    val newDieVal = Random.nextInt(1, 7)
    val newD1 = if (dieToReroll == 0) newDieVal else current.die1
    val newM = if (dieToReroll == 1) newDieVal else current.marvelDie
    val newD2 = if (dieToReroll == 2) newDieVal else current.die2

    val updatedResult = DiceRollResult(
      die1 = newD1,
      marvelDie = newM,
      die2 = newD2,
      modifier = current.modifier,
      targetNumber = current.targetNumber,
      label = "${current.label} (Reroll Die #${dieToReroll + 1})",
      edgeTroubleMode = _edgeTrouble.value,
      rerolledDieIndex = dieToReroll,
      originalDice = listOf(current.die1, current.marvelDie, current.die2)
    )

    _currentRoll.value = updatedResult
    _selectedDieForReroll.value = null
    repository.addRollResult(updatedResult)
  }

  fun rollForAbility(abilityName: String, abilityScore: Int) {
    _rollLabel.value = "$abilityName Check"
    _rollModifier.value = abilityScore
    rollDice()
  }

  fun clearRollHistory() {
    repository.clearRollHistory()
  }

  // --- Character Logic ---

  fun saveCharacter(character: CharacterProfile) {
    repository.saveCharacter(character)
  }

  fun duplicateCharacter(character: CharacterProfile) {
    repository.duplicateCharacter(character)
  }

  fun deleteCharacter(id: String) {
    repository.deleteCharacter(id)
  }

  fun adjustHealth(delta: Int) {
    val char = getActiveCharacter() ?: return
    repository.adjustHealth(char.id, delta)
  }

  fun adjustFocus(delta: Int) {
    val char = getActiveCharacter() ?: return
    repository.adjustFocus(char.id, delta)
  }

  fun adjustKarma(delta: Int) {
    val char = getActiveCharacter() ?: return
    repository.adjustKarma(char.id, delta)
  }

  fun toggleCondition(condition: String) {
    val char = getActiveCharacter() ?: return
    repository.toggleCondition(char.id, condition)
  }

  // --- Rulebook Logic ---

  fun saveRule(rule: RuleEntry) {
    repository.saveRule(rule)
  }

  fun deleteRule(id: String) {
    repository.deleteRule(id)
  }

  fun resetRulesToDefault() {
    repository.resetRulesToDefault()
  }

  // --- Generator Logic ---

  fun regenerateStory() {
    _currentStory.value = GeneratorTables.generateStory()
  }

  fun regenerateNpc() {
    _currentNpc.value = GeneratorTables.generateNpc()
  }

  fun regenerateEncounter() {
    _currentEncounter.value = GeneratorTables.generateEncounter()
  }

  fun saveStoryToLog() {
    val story = _currentStory.value
    repository.addSessionLog(
      title = "Story Prompt: ${story.hook.take(40)}...",
      content = story.toFormattedText(),
      type = SessionEntryType.GENERATOR
    )
  }

  fun saveNpcToLog() {
    val npc = _currentNpc.value
    repository.addSessionLog(
      title = "NPC: ${npc.codename} (${npc.name})",
      content = npc.toFormattedText(),
      type = SessionEntryType.GENERATOR
    )
  }

  fun saveEncounterToLog() {
    val enc = _currentEncounter.value
    repository.addSessionLog(
      title = "Encounter: ${enc.enemies.take(40)}...",
      content = enc.toFormattedText(),
      type = SessionEntryType.GENERATOR
    )
  }

  fun pinCurrentRollToLog() {
    val roll = _currentRoll.value ?: return
    repository.addSessionLog(
      title = "${roll.label}: ${roll.outcome.title}",
      content = "Roll: ${roll.mathBreakdown}\nOutcome: ${roll.outcome.title}",
      type = SessionEntryType.ROLL,
      metadata = roll.id
    )
  }

  // --- Session Log Logic ---

  fun addSessionNote(title: String, content: String) {
    repository.addSessionLog(title, content, SessionEntryType.NOTE)
  }

  fun deleteSessionLog(id: String) {
    repository.deleteSessionLog(id)
  }

  fun clearSessionLogs() {
    repository.clearSessionLogs()
  }

  // --- JSON Export / Import ---

  fun exportAllDataJson(): String = repository.exportAllDataAsJson()

  fun importAllDataJson(jsonStr: String): Boolean = repository.importAllDataFromJson(jsonStr)
}
