package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AbilityScores
import com.example.data.models.CharacterProfile
import com.example.data.models.EquipmentItem
import com.example.data.models.PowerItem
import com.example.data.models.STANDARD_ARCHETYPES
import com.example.data.models.STANDARD_CONDITIONS
import com.example.data.models.STANDARD_ORIGINS
import com.example.data.models.TraitItem
import com.example.ui.CompanionViewModel
import com.example.ui.components.ComicHeaderBanner
import com.example.ui.theme.BangersFont
import com.example.ui.theme.ComicBorder
import com.example.ui.theme.ComicCard
import com.example.ui.theme.ComicCardElevated
import com.example.ui.theme.ComicDarkBg
import com.example.ui.theme.ComicTextPrimary
import com.example.ui.theme.ComicTextSecondary
import com.example.ui.theme.FocusCyan
import com.example.ui.theme.HealthGreen
import com.example.ui.theme.HealthRed
import com.example.ui.theme.KarmaGold
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterSheetsScreen(
  viewModel: CompanionViewModel,
  onRollAbility: (String, Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val characters by viewModel.characters.collectAsState()
  val activeId by viewModel.activeCharacterId.collectAsState()
  val character = characters.find { it.id == activeId } ?: characters.firstOrNull()

  var showEditProfileDialog by remember { mutableStateOf(false) }
  var showNewCharacterDialog by remember { mutableStateOf(false) }
  var showDeleteConfirmDialog by remember { mutableStateOf(false) }
  var showAddPowerDialog by remember { mutableStateOf(false) }
  var showAddTraitDialog by remember { mutableStateOf(false) }
  var showAddTagDialog by remember { mutableStateOf(false) }
  var showAddEquipmentDialog by remember { mutableStateOf(false) }
  var showDerivedBreakdown by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ComicDarkBg)
      .testTag("character_sheets_screen"),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    item {
      ComicHeaderBanner(
        title = "HERO DOSSIER",
        subtitle = "Roster & Stat Sheet",
        badgeText = "RANK ${character?.rank ?: 3}"
      )
    }

    // Character Switcher & Roster Ribbon
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier
            .weight(1f)
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          characters.forEach { hero ->
            val isActive = hero.id == character?.id
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isActive) MarvelRed else ComicCard)
                .border(1.dp, if (isActive) MarvelGold else ComicBorder, RoundedCornerShape(8.dp))
                .clickable { viewModel.setActiveCharacter(hero.id) }
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .testTag("hero_tab_${hero.codename}")
            ) {
              Text(
                text = "${hero.codename} (R${hero.rank})",
                fontFamily = BangersFont,
                fontSize = 13.sp,
                color = if (isActive) Color.White else ComicTextSecondary
              )
            }
          }
        }

        IconButton(
          onClick = { showNewCharacterDialog = true },
          modifier = Modifier.testTag("add_character_button")
        ) {
          Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "Add Hero", tint = MarvelGold)
        }
      }
    }

    if (character != null) {
      // Hero Header Card (Name, Codename, Rank, Origin, Archetype, Actions)
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(1.5.dp, MarvelRed, RoundedCornerShape(14.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(14.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = character.codename.uppercase(),
                  fontFamily = BangersFont,
                  fontSize = 26.sp,
                  color = MarvelGold,
                  letterSpacing = 1.sp
                )
                Text(
                  text = "${character.name} • ${character.origin} • ${character.archetype}",
                  style = MaterialTheme.typography.bodyMedium,
                  color = Color.LightGray
                )
              }

              Row {
                IconButton(
                  onClick = { showEditProfileDialog = true },
                  modifier = Modifier.testTag("edit_hero_button")
                ) {
                  Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.LightGray)
                }
                IconButton(
                  onClick = { viewModel.duplicateCharacter(character) },
                  modifier = Modifier.testTag("duplicate_hero_button")
                ) {
                  Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = Color.LightGray)
                }
                IconButton(
                  onClick = { showDeleteConfirmDialog = true },
                  modifier = Modifier.testTag("delete_hero_button")
                ) {
                  Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = HealthRed)
                }
              }
            }
          }
        }
      }

      // Health, Focus, and Karma Interactive Trackers
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(1.dp, ComicBorder, RoundedCornerShape(14.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(14.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Health Bar
            val healthFraction = (character.currentHealth.toFloat() / character.maxHealth.toFloat()).coerceIn(0f, 1f)
            val hpColor by animateColorAsState(
              targetValue = when {
                healthFraction > 0.5f -> HealthGreen
                healthFraction > 0.25f -> Color(0xFFF59E0B)
                else -> HealthRed
              }, label = "hpColor"
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = HealthRed, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "HEALTH", fontFamily = BangersFont, fontSize = 16.sp, color = Color.White)
              }
              Text(
                text = "${character.currentHealth} / ${character.maxHealth} HP",
                fontFamily = BangersFont,
                fontSize = 16.sp,
                color = hpColor
              )
            }

            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
              progress = { healthFraction },
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
              color = hpColor,
              trackColor = ComicCardElevated,
            )

            // Health Quick Buttons
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              listOf(-10, -5, -1, 1, 5, 10).forEach { delta ->
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (delta < 0) Color(0xFF371E22) else Color(0xFF1E3728))
                    .border(1.dp, if (delta < 0) HealthRed.copy(alpha = 0.5f) else HealthGreen.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .clickable { viewModel.adjustHealth(delta) }
                    .padding(vertical = 4.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = if (delta > 0) "+$delta" else "$delta",
                    fontFamily = BangersFont,
                    fontSize = 12.sp,
                    color = if (delta < 0) HealthRed else HealthGreen
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Focus Bar
            val focusFraction = (character.currentFocus.toFloat() / character.maxFocus.toFloat()).coerceIn(0f, 1f)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.FlashOn, contentDescription = null, tint = FocusCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "FOCUS", fontFamily = BangersFont, fontSize = 16.sp, color = Color.White)
              }
              Text(
                text = "${character.currentFocus} / ${character.maxFocus} FOCUS",
                fontFamily = BangersFont,
                fontSize = 16.sp,
                color = FocusCyan
              )
            }

            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
              progress = { focusFraction },
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
              color = FocusCyan,
              trackColor = ComicCardElevated,
            )

            // Focus Quick Buttons
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              listOf(-10, -5, -1, 1, 5, 10).forEach { delta ->
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF162D3B))
                    .border(1.dp, FocusCyan.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .clickable { viewModel.adjustFocus(delta) }
                    .padding(vertical = 4.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = if (delta > 0) "+$delta" else "$delta",
                    fontFamily = BangersFont,
                    fontSize = 12.sp,
                    color = FocusCyan
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Karma Bar
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = KarmaGold, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "KARMA", fontFamily = BangersFont, fontSize = 16.sp, color = Color.White)
              }
              Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                  onClick = { viewModel.adjustKarma(-1) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Text("-", fontFamily = BangersFont, fontSize = 18.sp, color = KarmaGold)
                }
                Text(
                  text = "${character.currentKarma} / ${character.maxKarma}",
                  fontFamily = BangersFont,
                  fontSize = 17.sp,
                  color = KarmaGold,
                  modifier = Modifier.padding(horizontal = 6.dp)
                )
                IconButton(
                  onClick = { viewModel.adjustKarma(1) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Text("+", fontFamily = BangersFont, fontSize = 18.sp, color = KarmaGold)
                }
              }
            }
          }
        }
      }

      // Conditions Status Area
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(1.dp, ComicBorder, RoundedCornerShape(12.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "CONDITIONS & STATUSES",
              fontFamily = BangersFont,
              fontSize = 16.sp,
              color = MarvelGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              STANDARD_CONDITIONS.forEach { condition ->
                val isActive = character.conditions.contains(condition)
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isActive) HealthRed else ComicCardElevated)
                    .border(1.dp, if (isActive) Color.White else ComicBorder, RoundedCornerShape(6.dp))
                    .clickable { viewModel.toggleCondition(condition) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .testTag("condition_chip_$condition")
                ) {
                  Text(
                    text = condition,
                    fontFamily = BangersFont,
                    fontSize = 12.sp,
                    color = if (isActive) Color.White else Color.Gray
                  )
                }
              }
            }
          }
        }
      }

      // MARVEL Ability Scores & Defenses (Interactive)
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(1.dp, MarvelRed, RoundedCornerShape(14.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(14.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "M.A.R.V.E.L. ABILITIES",
                fontFamily = BangersFont,
                fontSize = 18.sp,
                color = MarvelGold
              )
              Text(
                text = "Tap to Roll D616",
                style = MaterialTheme.typography.bodySmall,
                color = ComicTextSecondary
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val abilityList = listOf(
              Triple("Melee", character.abilities.melee, character.defenseMelee),
              Triple("Agility", character.abilities.agility, character.defenseAgility),
              Triple("Resilience", character.abilities.resilience, character.defenseResilience),
              Triple("Vigilance", character.abilities.vigilance, character.defenseVigilance),
              Triple("Ego", character.abilities.ego, character.defenseEgo),
              Triple("Logic", character.abilities.logic, character.defenseLogic)
            )

            // 2 columns x 3 rows grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              for (i in abilityList.indices step 2) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  for (j in 0..1) {
                    val idx = i + j
                    if (idx < abilityList.size) {
                      val (name, score, defense) = abilityList[idx]
                      Box(
                        modifier = Modifier
                          .weight(1f)
                          .clip(RoundedCornerShape(10.dp))
                          .background(ComicCardElevated)
                          .border(1.dp, ComicBorder, RoundedCornerShape(10.dp))
                          .clickable { onRollAbility(name, score) }
                          .padding(10.dp)
                          .testTag("ability_box_$name")
                      ) {
                        Column {
                          Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                          ) {
                            Text(
                              text = name.uppercase(),
                              fontFamily = BangersFont,
                              fontSize = 15.sp,
                              color = MarvelGold
                            )
                            Icon(imageVector = Icons.Default.Casino, contentDescription = "Roll", tint = MarvelRed, modifier = Modifier.size(16.dp))
                          }
                          Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                          ) {
                            Text(
                              text = "+$score",
                              fontFamily = BangersFont,
                              fontSize = 22.sp,
                              color = Color.White
                            )
                            Text(
                              text = "Def: $defense",
                              fontFamily = BangersFont,
                              fontSize = 12.sp,
                              color = Color.LightGray
                            )
                          }
                        }
                      }
                    }
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Derived stats toggle breakdown
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { showDerivedBreakdown = !showDerivedBreakdown }
                .padding(vertical = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MarvelGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Auto-Calculated Derived Values", fontFamily = BangersFont, fontSize = 14.sp, color = MarvelGold)
              }
              Icon(
                imageVector = if (showDerivedBreakdown) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = MarvelGold
              )
            }

            AnimatedVisibility(visible = showDerivedBreakdown) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(ComicDarkBg, RoundedCornerShape(8.dp))
                  .border(0.5.dp, ComicBorder, RoundedCornerShape(8.dp))
                  .padding(10.dp)
              ) {
                Text("• ${character.healthBreakdown}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Text("• ${character.focusBreakdown}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Text("• ${character.damageBreakdown}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Text("• Initiative: ${character.initiativeBreakdown}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Text("• Speed: ${character.speed} ft / 5 spaces standard run", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Text("• Defenses: Score + 10 (Target for enemy action checks)", style = MaterialTheme.typography.bodySmall, color = Color.White)
              }
            }
          }
        }
      }

      // Powers Section
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(1.dp, ComicBorder, RoundedCornerShape(12.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "POWERS (${character.powers.size})",
                fontFamily = BangersFont,
                fontSize = 18.sp,
                color = MarvelGold
              )
              IconButton(
                onClick = { showAddPowerDialog = true },
                modifier = Modifier.testTag("add_power_button")
              ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Power", tint = MarvelGold)
              }
            }

            if (character.powers.isEmpty()) {
              Text(
                text = "No powers listed yet. Tap + to add powers.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 6.dp)
              )
            } else {
              character.powers.forEach { power ->
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = ComicCardElevated,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(0.5.dp, ComicBorder, RoundedCornerShape(8.dp))
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column(modifier = Modifier.weight(1f)) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                          text = power.name,
                          fontFamily = BangersFont,
                          fontSize = 15.sp,
                          color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                          modifier = Modifier
                            .background(MarvelRed, RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                          Text(text = power.powerSet, fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                      }
                      Text(
                        text = "${power.actionType} • Cost: ${power.cost} Focus • Range: ${power.range}",
                        style = MaterialTheme.typography.bodySmall,
                        color = FocusCyan
                      )
                      if (power.description.isNotBlank()) {
                        Text(
                          text = power.description,
                          style = MaterialTheme.typography.bodySmall,
                          color = ComicTextSecondary
                        )
                      }
                    }

                    IconButton(
                      onClick = {
                        val updated = character.powers.filter { it.id != power.id }
                        viewModel.saveCharacter(character.copy(powers = updated))
                      }
                    ) {
                      Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    }
                  }
                }
              }
            }
          }
        }
      }

      // Traits & Tags Section
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(1.dp, ComicBorder, RoundedCornerShape(12.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Traits Header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "TRAITS (${character.traits.size})",
                fontFamily = BangersFont,
                fontSize = 16.sp,
                color = MarvelGold
              )
              IconButton(onClick = { showAddTraitDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Trait", tint = MarvelGold)
              }
            }

            character.traits.forEach { trait ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(text = trait.name, fontFamily = BangersFont, fontSize = 14.sp, color = Color.White)
                  if (trait.description.isNotBlank()) {
                    Text(text = trait.description, style = MaterialTheme.typography.bodySmall, color = ComicTextSecondary)
                  }
                }
                IconButton(
                  onClick = {
                    val updated = character.traits.filter { it.id != trait.id }
                    viewModel.saveCharacter(character.copy(traits = updated))
                  }
                ) {
                  Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tags Header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "TAGS",
                fontFamily = BangersFont,
                fontSize = 16.sp,
                color = MarvelGold
              )
              IconButton(onClick = { showAddTagDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Tag", tint = MarvelGold)
              }
            }

            FlowRow(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              character.tags.forEach { tag ->
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ComicCardElevated)
                    .border(1.dp, ComicBorder, RoundedCornerShape(6.dp))
                    .clickable {
                      val updated = character.tags.filter { it != tag }
                      viewModel.saveCharacter(character.copy(tags = updated))
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(text = "$tag ×", fontFamily = BangersFont, fontSize = 12.sp, color = Color.LightGray)
                }
              }
            }
          }
        }
      }

      // Equipment Section
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(1.dp, ComicBorder, RoundedCornerShape(12.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "EQUIPMENT & GEAR (${character.equipment.size})",
                fontFamily = BangersFont,
                fontSize = 16.sp,
                color = MarvelGold
              )
              IconButton(onClick = { showAddEquipmentDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Equipment", tint = MarvelGold)
              }
            }

            character.equipment.forEach { gear ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(text = "${gear.name} [${gear.type}]", fontFamily = BangersFont, fontSize = 14.sp, color = Color.White)
                  if (gear.effect.isNotBlank()) {
                    Text(text = gear.effect, style = MaterialTheme.typography.bodySmall, color = ComicTextSecondary)
                  }
                }
                IconButton(
                  onClick = {
                    val updated = character.equipment.filter { it.id != gear.id }
                    viewModel.saveCharacter(character.copy(equipment = updated))
                  }
                ) {
                  Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
              }
            }
          }
        }
      }

      // Bio / Free-Text Notes Section
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .border(1.dp, ComicBorder, RoundedCornerShape(12.dp)),
          colors = CardDefaults.cardColors(containerColor = ComicCard),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "BACKSTORY & NOTES",
              fontFamily = BangersFont,
              fontSize = 16.sp,
              color = MarvelGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
              value = character.notes,
              onValueChange = { viewModel.saveCharacter(character.copy(notes = it)) },
              placeholder = { Text("Jot down hero backstory, allies, nemesis, base of operations...", color = Color.Gray) },
              modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .testTag("hero_notes_field"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = ComicTextPrimary,
                unfocusedTextColor = ComicTextPrimary,
                focusedBorderColor = MarvelRed,
                unfocusedBorderColor = ComicBorder
              )
            )
          }
        }
      }
    }
  }

  // Edit Profile Dialog
  if (showEditProfileDialog && character != null) {
    EditCharacterDialog(
      initial = character,
      onDismiss = { showEditProfileDialog = false },
      onSave = { updated ->
        viewModel.saveCharacter(updated)
        showEditProfileDialog = false
      }
    )
  }

  // New Character Dialog
  if (showNewCharacterDialog) {
    EditCharacterDialog(
      initial = CharacterProfile(
        id = java.util.UUID.randomUUID().toString(),
        name = "New Hero",
        codename = "Superhero",
        rank = 3
      ),
      isNew = true,
      onDismiss = { showNewCharacterDialog = false },
      onSave = { newHero ->
        viewModel.saveCharacter(newHero)
        viewModel.setActiveCharacter(newHero.id)
        showNewCharacterDialog = false
      }
    )
  }

  // Delete Confirm Dialog
  if (showDeleteConfirmDialog && character != null) {
    AlertDialog(
      onDismissRequest = { showDeleteConfirmDialog = false },
      title = { Text("Delete ${character.codename}?", fontFamily = BangersFont, fontSize = 20.sp, color = MarvelGold) },
      text = { Text("Are you sure you want to retire this hero sheet? This action cannot be undone.", color = ComicTextPrimary) },
      confirmButton = {
        Button(
          onClick = {
            viewModel.deleteCharacter(character.id)
            showDeleteConfirmDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = HealthRed)
        ) {
          Text("DELETE", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteConfirmDialog = false }) {
          Text("CANCEL", color = Color.Gray, fontFamily = BangersFont)
        }
      },
      containerColor = ComicCard
    )
  }

  // Add Power Dialog
  if (showAddPowerDialog && character != null) {
    var pName by remember { mutableStateOf("") }
    var pSet by remember { mutableStateOf("Basic") }
    var pAction by remember { mutableStateOf("Standard") }
    var pCost by remember { mutableStateOf("5") }
    var pRange by remember { mutableStateOf("Reach") }
    var pDesc by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showAddPowerDialog = false },
      title = { Text("ADD SUPER POWER", fontFamily = BangersFont, fontSize = 20.sp, color = MarvelGold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = pName, onValueChange = { pName = it }, label = { Text("Power Name") }, singleLine = true)
          OutlinedTextField(value = pSet, onValueChange = { pSet = it }, label = { Text("Power Set (e.g. Energy, Brawling)") }, singleLine = true)
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = pAction, onValueChange = { pAction = it }, label = { Text("Action") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = pCost, onValueChange = { pCost = it }, label = { Text("Focus Cost") }, modifier = Modifier.weight(1f))
          }
          OutlinedTextField(value = pRange, onValueChange = { pRange = it }, label = { Text("Range / Area") })
          OutlinedTextField(value = pDesc, onValueChange = { pDesc = it }, label = { Text("Power Effect / Rules") })
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (pName.isNotBlank()) {
              val newPower = PowerItem(
                name = pName,
                powerSet = pSet,
                actionType = pAction,
                cost = pCost.toIntOrNull() ?: 0,
                range = pRange,
                description = pDesc
              )
              viewModel.saveCharacter(character.copy(powers = character.powers + newPower))
            }
            showAddPowerDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
        ) {
          Text("ADD POWER", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddPowerDialog = false }) { Text("CANCEL", color = Color.Gray, fontFamily = BangersFont) }
      },
      containerColor = ComicCard
    )
  }

  // Add Trait Dialog
  if (showAddTraitDialog && character != null) {
    var tName by remember { mutableStateOf("") }
    var tDesc by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { showAddTraitDialog = false },
      title = { Text("ADD TRAIT", fontFamily = BangersFont, fontSize = 20.sp, color = MarvelGold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = tName, onValueChange = { tName = it }, label = { Text("Trait Name (e.g. Combat Reflexes)") })
          OutlinedTextField(value = tDesc, onValueChange = { tDesc = it }, label = { Text("Trait Description / Perk") })
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (tName.isNotBlank()) {
              viewModel.saveCharacter(character.copy(traits = character.traits + TraitItem(name = tName, description = tDesc)))
            }
            showAddTraitDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
        ) {
          Text("ADD TRAIT", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddTraitDialog = false }) { Text("CANCEL", color = Color.Gray, fontFamily = BangersFont) }
      },
      containerColor = ComicCard
    )
  }

  // Add Tag Dialog
  if (showAddTagDialog && character != null) {
    var tagName by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { showAddTagDialog = false },
      title = { Text("ADD TAG", fontFamily = BangersFont, fontSize = 20.sp, color = MarvelGold) },
      text = {
        OutlinedTextField(value = tagName, onValueChange = { tagName = it }, label = { Text("Tag (e.g. Heroic, Secret Identity)") })
      },
      confirmButton = {
        Button(
          onClick = {
            if (tagName.isNotBlank() && !character.tags.contains(tagName)) {
              viewModel.saveCharacter(character.copy(tags = character.tags + tagName))
            }
            showAddTagDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
        ) {
          Text("ADD TAG", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddTagDialog = false }) { Text("CANCEL", color = Color.Gray, fontFamily = BangersFont) }
      },
      containerColor = ComicCard
    )
  }

  // Add Equipment Dialog
  if (showAddEquipmentDialog && character != null) {
    var eqName by remember { mutableStateOf("") }
    var eqType by remember { mutableStateOf("Gear") }
    var eqEffect by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { showAddEquipmentDialog = false },
      title = { Text("ADD EQUIPMENT", fontFamily = BangersFont, fontSize = 20.sp, color = MarvelGold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = eqName, onValueChange = { eqName = it }, label = { Text("Item Name") })
          OutlinedTextField(value = eqType, onValueChange = { eqType = it }, label = { Text("Type (Suit, Weapon, Gadget)") })
          OutlinedTextField(value = eqEffect, onValueChange = { eqEffect = it }, label = { Text("Game Effect") })
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (eqName.isNotBlank()) {
              viewModel.saveCharacter(character.copy(equipment = character.equipment + EquipmentItem(name = eqName, type = eqType, effect = eqEffect)))
            }
            showAddEquipmentDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
        ) {
          Text("ADD GEAR", fontFamily = BangersFont)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddEquipmentDialog = false }) { Text("CANCEL", color = Color.Gray, fontFamily = BangersFont) }
      },
      containerColor = ComicCard
    )
  }
}

@Composable
private fun EditCharacterDialog(
  initial: CharacterProfile,
  isNew: Boolean = false,
  onDismiss: () -> Unit,
  onSave: (CharacterProfile) -> Unit
) {
  var name by remember { mutableStateOf(initial.name) }
  var codename by remember { mutableStateOf(initial.codename) }
  var rank by remember { mutableStateOf(initial.rank) }
  var origin by remember { mutableStateOf(initial.origin) }
  var archetype by remember { mutableStateOf(initial.archetype) }

  var melee by remember { mutableStateOf(initial.abilities.melee) }
  var agility by remember { mutableStateOf(initial.abilities.agility) }
  var resilience by remember { mutableStateOf(initial.abilities.resilience) }
  var vigilance by remember { mutableStateOf(initial.abilities.vigilance) }
  var ego by remember { mutableStateOf(initial.abilities.ego) }
  var logic by remember { mutableStateOf(initial.abilities.logic) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (isNew) "CREATE NEW SUPERHERO" else "EDIT HERO PROFILE",
        fontFamily = BangersFont,
        fontSize = 22.sp,
        color = MarvelGold
      )
    },
    text = {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .height(420.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          OutlinedTextField(value = codename, onValueChange = { codename = it }, label = { Text("Hero Codename") }, singleLine = true)
        }
        item {
          OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Real Name / Identity") }, singleLine = true)
        }
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Rank (1-6):", fontFamily = BangersFont, fontSize = 16.sp, color = Color.White)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              (1..6).forEach { r ->
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (rank == r) MarvelRed else ComicCardElevated)
                    .clickable { rank = r }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(text = "$r", fontFamily = BangersFont, color = if (rank == r) Color.White else Color.Gray)
                }
              }
            }
          }
        }
        item {
          OutlinedTextField(value = origin, onValueChange = { origin = it }, label = { Text("Origin (e.g. Mutant, High-Tech, Magic)") }, singleLine = true)
        }
        item {
          OutlinedTextField(value = archetype, onValueChange = { archetype = it }, label = { Text("Archetype (Striker, Blaster, Bruiser...)") }, singleLine = true)
        }
        item {
          Text("M.A.R.V.E.L. Ability Scores:", fontFamily = BangersFont, fontSize = 16.sp, color = MarvelGold, modifier = Modifier.padding(top = 8.dp))
        }
        item {
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            AbilityScoreRow("Melee", melee) { melee = it }
            AbilityScoreRow("Agility", agility) { agility = it }
            AbilityScoreRow("Resilience", resilience) { resilience = it }
            AbilityScoreRow("Vigilance", vigilance) { vigilance = it }
            AbilityScoreRow("Ego", ego) { ego = it }
            AbilityScoreRow("Logic", logic) { logic = it }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val updated = initial.copy(
            name = name,
            codename = codename,
            rank = rank,
            origin = origin,
            archetype = archetype,
            abilities = AbilityScores(melee, agility, resilience, vigilance, ego, logic),
            currentHealth = if (isNew) resilience * 30 else initial.currentHealth.coerceAtMost(resilience * 30),
            currentFocus = if (isNew) vigilance * 30 else initial.currentFocus.coerceAtMost(vigilance * 30),
            currentKarma = if (isNew) rank else initial.currentKarma
          )
          onSave(updated)
        },
        colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
      ) {
        Text("SAVE PROFILE", fontFamily = BangersFont)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("CANCEL", color = Color.Gray, fontFamily = BangersFont) }
    },
    containerColor = ComicCard
  )
}

@Composable
private fun AbilityScoreRow(name: String, value: Int, onChange: (Int) -> Unit) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = "$name (+${value + 10} Def)", style = MaterialTheme.typography.bodyMedium, color = Color.White)
    Row(verticalAlignment = Alignment.CenterVertically) {
      IconButton(onClick = { onChange((value - 1).coerceAtLeast(0)) }, modifier = Modifier.size(28.dp)) {
        Text("-", fontFamily = BangersFont, fontSize = 16.sp, color = MarvelGold)
      }
      Text(text = "$value", fontFamily = BangersFont, fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp))
      IconButton(onClick = { onChange(value + 1) }, modifier = Modifier.size(28.dp)) {
        Text("+", fontFamily = BangersFont, fontSize = 16.sp, color = MarvelGold)
      }
    }
  }
}
