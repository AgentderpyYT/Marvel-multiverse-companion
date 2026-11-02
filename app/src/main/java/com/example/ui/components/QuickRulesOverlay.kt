package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.BangersFont
import com.example.ui.theme.ComicBorder
import com.example.ui.theme.ComicCard
import com.example.ui.theme.ComicCardElevated
import com.example.ui.theme.ComicDarkBg
import com.example.ui.theme.ComicTextPrimary
import com.example.ui.theme.ComicTextSecondary
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed

data class QuickRuleSection(
  val id: String,
  val title: String,
  val icon: String,
  val items: List<Pair<String, String>>
)

val QUICK_RULES_DATA = listOf(
  QuickRuleSection(
    id = "combat_actions",
    title = "Combat Actions & Turn Economy",
    icon = "⚔️",
    items = listOf(
      "Standard Action" to "Attack, activate attack powers, field first aid, aid ally, or operate complex devices.",
      "Movement Action" to "Move up to Run Speed (standard 25 ft / 5 spaces). Powers like Flight or Super-Speed modify this.",
      "Reactions (1 / round)" to "Triggered outside your turn to dodge attacks, trigger protective force fields, or opportunity strikes.",
      "Free Actions" to "Brief speech, dropping held gear, or minor sensory gestures."
    )
  ),
  QuickRuleSection(
    id = "edge_trouble",
    title = "Advantage & Disadvantage (Edge & Trouble)",
    icon = "⚖️",
    items = listOf(
      "Edge (Advantage)" to "Reroll any one die of your choice and keep the new result.",
      "Trouble (Disadvantage)" to "Forced to reroll one die (usually the Marvel die or highest die) and take the lower outcome.",
      "Stacking & Cancellation" to "Edge and Trouble cancel out 1-to-1. Multiple instances allow multiple single-die rerolls up to the 3 dice rolled."
    )
  ),
  QuickRuleSection(
    id = "power_usage",
    title = "Basic Power Usage & Activation",
    icon = "⚡",
    items = listOf(
      "Focus Cost & Activation" to "Most super powers require spending Focus points. At 0 Focus, you cannot activate powers requiring Focus.",
      "Action Types" to "Standard (attacks), Movement (flight, phasing), Reaction (defenses), or Permanent (innate armor).",
      "Power Sets" to "Martial Arts, Super-Strength, Energy Control, Telepathy, Phasing, Elemental, Tactics. [User placeholder: Add custom sets]."
    )
  ),
  QuickRuleSection(
    id = "d616_checks",
    title = "Ability Checks & The d616 Roll",
    icon = "🎲",
    items = listOf(
      "The 3d6 Roll & Marvel Die" to "Action checks roll 3d6 (2 standard d6 + 1 Marvel Die). When the Marvel Die lands on 1, it counts as a 6 in your sum and triggers Fantastic outcomes!",
      "Action Check Total" to "Total = Die 1 + Marvel Die (1 counts as 6) + Die 2 + Ability Modifier vs Target Number (TN).",
      "Fantastic Success & 616" to "Marvel Die 1 + Total >= TN is a Fantastic Success (extra damage & power triggers). Rolling 6-1-6 is an Ultimate Fantastic Roll (immediate triumph)!"
    )
  ),
  QuickRuleSection(
    id = "damage_health",
    title = "Damage, Health & Recovery",
    icon = "❤️",
    items = listOf(
      "Health vs Focus Pools" to "Max Health = Resilience x 30. Max Focus = Vigilance x 30. Health damage knocks you Unconscious; Focus damage Demoralizes.",
      "Damage Multipliers" to "Base Damage = Marvel Die value x Rank Multiplier + Ability Modifier. Fantastic Successes multiply or maximize total damage.",
      "Recovery" to "Focus recovers during short breathers; Health recovery requires downtime, medical care, or healing powers."
    )
  ),
  QuickRuleSection(
    id = "karma_rules",
    title = "Karma & Common Uses",
    icon = "⭐",
    items = listOf(
      "Karma Pool" to "Max Karma equals your Rank. Earn Karma through heroic deeds, overcoming complications, or self-sacrifice.",
      "Common Uses" to "Spend 1 Karma to reroll any one die, add an Edge to a crucial check, reduce incoming damage by 10, or assist an ally."
    )
  )
)

@Composable
fun QuickRulesOverlay(
  onDismiss: () -> Unit
) {
  val expandedSections = remember { mutableStateMapOf<String, Boolean>() }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .padding(16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(ComicDarkBg)
        .border(2.dp, MarvelGold, RoundedCornerShape(16.dp))
        .testTag("quick_rules_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .heightIn(max = 620.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "⚡", fontSize = 20.sp)
            Spacer(modifier = Modifier.size(6.dp))
            Text(
              text = "QUICK RULES (D616 SYSTEM)",
              fontFamily = BangersFont,
              fontSize = 20.sp,
              color = MarvelGold,
              letterSpacing = 0.5.sp
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_quick_rules_button")
          ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.LightGray)
          }
        }

        // Community Notice
        Card(
          colors = CardDefaults.cardColors(containerColor = ComicCardElevated),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
        ) {
          Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MarvelGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(
              text = "Concise summaries of common game mechanics. Content is user-supplied and configurable.",
              style = MaterialTheme.typography.bodySmall,
              color = ComicTextSecondary,
              fontSize = 11.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Collapsible Accordion Sections
        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(QUICK_RULES_DATA) { section ->
            val isExpanded = expandedSections[section.id] ?: false

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = ComicCard,
              modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, if (isExpanded) MarvelGold else ComicBorder, RoundedCornerShape(10.dp))
            ) {
              Column {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedSections[section.id] = !isExpanded }
                    .padding(12.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "${section.icon} ${section.title.uppercase()}",
                    fontFamily = BangersFont,
                    fontSize = 15.sp,
                    color = if (isExpanded) MarvelGold else Color.White
                  )

                  Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MarvelGold
                  )
                }

                AnimatedVisibility(visible = isExpanded) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .background(ComicDarkBg.copy(alpha = 0.5f))
                      .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    section.items.forEach { (heading, content) ->
                      Column {
                        Text(
                          text = heading,
                          fontFamily = BangersFont,
                          fontSize = 13.sp,
                          color = MarvelGold
                        )
                        Text(
                          text = content,
                          style = MaterialTheme.typography.bodySmall,
                          color = ComicTextPrimary,
                          lineHeight = 16.sp
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

        // Done Button
        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = MarvelRed),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("GOT IT, TRUE BELIEVER!", fontFamily = BangersFont, fontSize = 15.sp)
        }
      }
    }
  }
}
