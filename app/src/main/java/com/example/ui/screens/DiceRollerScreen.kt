package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DiceRollResult
import com.example.data.models.EdgeTroubleMode
import com.example.data.models.RollOutcome
import com.example.ui.CompanionViewModel
import com.example.ui.components.ComicHeaderBanner
import com.example.ui.components.D6DieView
import com.example.ui.components.OutcomeBadge
import com.example.ui.theme.BangersFont
import com.example.ui.theme.ComicBorder
import com.example.ui.theme.ComicCard
import com.example.ui.theme.ComicCardElevated
import com.example.ui.theme.ComicDarkBg
import com.example.ui.theme.ComicTextPrimary
import com.example.ui.theme.ComicTextSecondary
import com.example.ui.theme.DieMarvelGold
import com.example.ui.theme.DieMarvelRed
import com.example.ui.theme.FantasticGlow
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed

@Composable
fun DiceRollerScreen(
  viewModel: CompanionViewModel,
  modifier: Modifier = Modifier
) {
  val currentRoll by viewModel.currentRoll.collectAsState()
  val modifierVal by viewModel.rollModifier.collectAsState()
  val targetNumber by viewModel.targetNumber.collectAsState()
  val rollLabel by viewModel.rollLabel.collectAsState()
  val edgeTrouble by viewModel.edgeTrouble.collectAsState()
  val selectedDieForReroll by viewModel.selectedDieForReroll.collectAsState()
  val isRolling by viewModel.isRollingAnimation.collectAsState()
  val rollHistory by viewModel.rollHistory.collectAsState()
  val activeChar = viewModel.getActiveCharacter()

  var showPinSnackbar by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ComicDarkBg)
      .testTag("dice_roller_screen"),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    item {
      ComicHeaderBanner(
        title = "D616 DICE ROLLER",
        subtitle = "3D6 with Marvel Die • 1 is Fantastic (Counts as 6)",
        badgeText = if (activeChar != null) activeChar.codename else "HERO MODE"
      )
    }

    // Quick Roll Bar from Active Character
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp)
      ) {
        Text(
          text = "QUICK CHECKS ${if (activeChar != null) "(${activeChar.codename})" else ""}",
          fontFamily = BangersFont,
          fontSize = 14.sp,
          color = MarvelGold,
          letterSpacing = 0.5.sp,
          modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          val abilities = listOf(
            "Melee" to (activeChar?.abilities?.melee ?: 2),
            "Agility" to (activeChar?.abilities?.agility ?: 3),
            "Resilience" to (activeChar?.abilities?.resilience ?: 2),
            "Vigilance" to (activeChar?.abilities?.vigilance ?: 2),
            "Ego" to (activeChar?.abilities?.ego ?: 2),
            "Logic" to (activeChar?.abilities?.logic ?: 2),
            "Initiative" to (activeChar?.initiativeMod ?: 2)
          )

          abilities.forEach { (name, score) ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(ComicCard)
                .border(1.dp, ComicBorder, RoundedCornerShape(8.dp))
                .clickable { viewModel.rollForAbility(name, score) }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("quick_check_$name")
            ) {
              Text(
                text = "$name +$score",
                fontFamily = BangersFont,
                fontSize = 13.sp,
                color = ComicTextPrimary
              )
            }
          }
        }
      }
    }

    // Main Dice Arena Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp)
          .border(2.dp, if (currentRoll?.isFantastic == true) FantasticGlow else MarvelRed, RoundedCornerShape(16.dp))
          .shadow(8.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ComicCard),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Roll Title Label
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = rollLabel.uppercase(),
              fontFamily = BangersFont,
              fontSize = 18.sp,
              color = MarvelGold,
              letterSpacing = 0.5.sp
            )
            if (currentRoll != null) {
              Text(
                text = currentRoll!!.formattedTime,
                style = MaterialTheme.typography.bodySmall,
                color = ComicTextSecondary
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 3 Dice Display (Die 1, Marvel Die, Die 2)
          if (currentRoll != null) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly,
              verticalAlignment = Alignment.CenterVertically
            ) {
              D6DieView(
                value = currentRoll!!.die1,
                isMarvelDie = false,
                isSelectedForReroll = selectedDieForReroll == 0,
                size = 78.dp,
                onClick = { viewModel.selectDieForReroll(0) },
                isRolling = isRolling,
                testTag = "die_1"
              )

              D6DieView(
                value = currentRoll!!.marvelDie,
                isMarvelDie = true,
                isSelectedForReroll = selectedDieForReroll == 1,
                size = 86.dp,
                onClick = { viewModel.selectDieForReroll(1) },
                isRolling = isRolling,
                testTag = "marvel_die"
              )

              D6DieView(
                value = currentRoll!!.die2,
                isMarvelDie = false,
                isSelectedForReroll = selectedDieForReroll == 2,
                size = 78.dp,
                onClick = { viewModel.selectDieForReroll(2) },
                isRolling = isRolling,
                testTag = "die_2"
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Reroll action if a die is selected
          AnimatedVisibility(visible = selectedDieForReroll != null) {
            Button(
              onClick = { viewModel.rerollSelectedDie() },
              colors = ButtonDefaults.buttonColors(containerColor = MarvelGold, contentColor = ComicDarkBg),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .padding(vertical = 4.dp)
                .testTag("reroll_selected_button")
            ) {
              Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "REROLL DIE #${(selectedDieForReroll ?: 0) + 1}",
                fontFamily = BangersFont,
                fontSize = 15.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Outcome Banner and Total
          if (currentRoll != null) {
            val roll = currentRoll!!
            OutcomeBadge(outcome = roll.outcome)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "TOTAL: ${roll.total}",
              fontFamily = BangersFont,
              fontSize = 34.sp,
              color = if (roll.isFantastic) MarvelGold else Color.White,
              letterSpacing = 1.sp
            )

            Text(
              text = roll.mathBreakdown,
              fontFamily = BangersFont,
              fontSize = 15.sp,
              color = Color.LightGray,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = {
                  viewModel.pinCurrentRollToLog()
                  showPinSnackbar = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = ComicCardElevated, contentColor = MarvelGold),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("pin_roll_button")
              ) {
                Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (showPinSnackbar) "SAVED TO LOG!" else "SAVE TO LOG",
                  fontFamily = BangersFont,
                  fontSize = 14.sp
                )
              }
            }
          }
        }
      }
    }

    // Roll Configuration Controls Card
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
          // Roll Label Input
          OutlinedTextField(
            value = rollLabel,
            onValueChange = { viewModel.setRollLabel(it) },
            label = { Text("Roll Label / Action", color = Color.Gray, fontSize = 12.sp) },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = ComicTextPrimary,
              unfocusedTextColor = ComicTextPrimary,
              focusedBorderColor = MarvelRed,
              unfocusedBorderColor = ComicBorder
            ),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("roll_label_input")
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Advantage / Disadvantage Mode (Edge / Trouble)
          Text(
            text = "EDGE / TROUBLE (D616 REROLLS)",
            fontFamily = BangersFont,
            fontSize = 14.sp,
            color = MarvelGold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(
              EdgeTroubleMode.NONE to "NORMAL",
              EdgeTroubleMode.EDGE to "EDGE (Reroll 1)",
              EdgeTroubleMode.TROUBLE to "TROUBLE (Worse)"
            ).forEach { (mode, title) ->
              val isSelected = edgeTrouble == mode
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) MarvelRed else ComicCardElevated)
                  .border(1.dp, if (isSelected) MarvelGold else ComicBorder, RoundedCornerShape(8.dp))
                  .clickable { viewModel.setEdgeTrouble(mode) }
                  .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = title,
                  fontFamily = BangersFont,
                  fontSize = 12.sp,
                  color = if (isSelected) Color.White else Color.Gray,
                  textAlign = TextAlign.Center
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Modifier Stepper & Target Number
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Modifier
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "MODIFIER",
                fontFamily = BangersFont,
                fontSize = 13.sp,
                color = MarvelGold
              )
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                  .fillMaxWidth()
                  .background(ComicCardElevated, RoundedCornerShape(8.dp))
                  .border(1.dp, ComicBorder, RoundedCornerShape(8.dp))
                  .padding(4.dp)
              ) {
                IconButton(
                  onClick = { viewModel.adjustModifier(-1) },
                  modifier = Modifier.testTag("mod_minus_button")
                ) {
                  Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", tint = MarvelGold)
                }
                Text(
                  text = if (modifierVal >= 0) "+$modifierVal" else "$modifierVal",
                  fontFamily = BangersFont,
                  fontSize = 20.sp,
                  color = Color.White
                )
                IconButton(
                  onClick = { viewModel.adjustModifier(1) },
                  modifier = Modifier.testTag("mod_plus_button")
                ) {
                  Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = MarvelGold)
                }
              }
            }

            // Target Number (TN)
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "TARGET NUMBER (TN)",
                fontFamily = BangersFont,
                fontSize = 13.sp,
                color = MarvelGold
              )
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                  .fillMaxWidth()
                  .background(ComicCardElevated, RoundedCornerShape(8.dp))
                  .border(1.dp, ComicBorder, RoundedCornerShape(8.dp))
                  .padding(4.dp)
              ) {
                IconButton(
                  onClick = {
                    val curr = targetNumber ?: 15
                    viewModel.setTargetNumber((curr - 5).coerceAtLeast(5))
                  },
                  modifier = Modifier.testTag("tn_minus_button")
                ) {
                  Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease TN", tint = MarvelGold)
                }
                Text(
                  text = targetNumber?.toString() ?: "None",
                  fontFamily = BangersFont,
                  fontSize = 20.sp,
                  color = Color.White
                )
                IconButton(
                  onClick = {
                    val curr = targetNumber ?: 15
                    viewModel.setTargetNumber((curr + 5).coerceAtMost(40))
                  },
                  modifier = Modifier.testTag("tn_plus_button")
                ) {
                  Icon(imageVector = Icons.Default.Add, contentDescription = "Increase TN", tint = MarvelGold)
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Quick TN presets
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf(
              null to "Off",
              10 to "TN 10 (Easy)",
              15 to "TN 15 (Std)",
              20 to "TN 20 (Hard)",
              25 to "TN 25 (Ext)"
            ).forEach { (tn, label) ->
              val isSelected = targetNumber == tn
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (isSelected) MarvelGold else ComicCardElevated)
                  .border(1.dp, if (isSelected) Color.White else ComicBorder, RoundedCornerShape(6.dp))
                  .clickable { viewModel.setTargetNumber(tn) }
                  .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = label,
                  fontFamily = BangersFont,
                  fontSize = 11.sp,
                  color = if (isSelected) ComicDarkBg else Color.Gray,
                  textAlign = TextAlign.Center
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Giant ROLL D616 Button
          Button(
            onClick = { viewModel.rollDice() },
            colors = ButtonDefaults.buttonColors(
              containerColor = MarvelRed,
              contentColor = Color.White
            ),
            shape = CutCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .shadow(6.dp, CutCornerShape(10.dp))
              .testTag("roll_d616_button")
          ) {
            Icon(imageVector = Icons.Default.Casino, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "ROLL D616!",
              fontFamily = BangersFont,
              fontSize = 24.sp,
              letterSpacing = 1.2.sp
            )
          }
        }
      }
    }

    // Roll History Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ROLL HISTORY (${rollHistory.size})",
          fontFamily = BangersFont,
          fontSize = 16.sp,
          color = MarvelGold
        )
        if (rollHistory.isNotEmpty()) {
          TextButton(
            onClick = { viewModel.clearRollHistory() },
            modifier = Modifier.testTag("clear_history_button")
          ) {
            Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Clear", color = Color.Gray, fontSize = 12.sp)
          }
        }
      }
    }

    // Roll History Items
    items(rollHistory.take(15)) { roll ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp)
          .border(0.5.dp, ComicBorder, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = ComicCard),
        shape = RoundedCornerShape(8.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = roll.label,
              fontFamily = BangersFont,
              fontSize = 15.sp,
              color = ComicTextPrimary
            )
            Text(
              text = "${roll.formattedTime} • ${roll.mathBreakdown}",
              style = MaterialTheme.typography.bodySmall,
              color = ComicTextSecondary
            )
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "${roll.total}",
              fontFamily = BangersFont,
              fontSize = 20.sp,
              color = if (roll.isFantastic) MarvelGold else Color.White
            )
            Text(
              text = if (roll.isFantastic) "FANTASTIC" else if (roll.targetNumber != null && roll.total >= roll.targetNumber) "PASS" else "CHECK",
              fontFamily = BangersFont,
              fontSize = 11.sp,
              color = if (roll.isFantastic) FantasticGlow else Color.Gray
            )
          }
        }
      }
    }
  }
}
