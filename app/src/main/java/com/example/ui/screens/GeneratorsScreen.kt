package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.HealthRed
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed

@Composable
fun GeneratorsScreen(
  viewModel: CompanionViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableStateOf(0) } // 0: Story, 1: NPC, 2: Encounter

  val currentStory by viewModel.currentStory.collectAsState()
  val currentNpc by viewModel.currentNpc.collectAsState()
  val currentEncounter by viewModel.currentEncounter.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ComicDarkBg)
      .testTag("generators_screen"),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    item {
      ComicHeaderBanner(
        title = "NARRATIVE GENERATORS",
        subtitle = "Instant Prompts, NPC Rogues & Battle Hazards",
        badgeText = "RANDOM TABLES"
      )
    }

    // Tabs: Story, NPC, Encounter
    item {
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = ComicCard,
        contentColor = MarvelGold,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = MarvelGold,
            height = 3.dp
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .clip(RoundedCornerShape(8.dp))
          .border(1.dp, ComicBorder, RoundedCornerShape(8.dp))
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("STORY ELEMENTS", fontFamily = BangersFont, fontSize = 14.sp) },
          modifier = Modifier.testTag("story_tab")
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("NPC / VILLAINS", fontFamily = BangersFont, fontSize = 14.sp) },
          modifier = Modifier.testTag("npc_tab")
        )
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = { Text("ENCOUNTERS", fontFamily = BangersFont, fontSize = 14.sp) },
          modifier = Modifier.testTag("encounter_tab")
        )
      }
    }

    // Generator Body
    when (selectedTab) {
      0 -> {
        // STORY ELEMENTS
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp)
              .border(1.5.dp, MarvelRed, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = ComicCard),
            shape = RoundedCornerShape(14.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "MULTIVERSE PLOT HOOK & TWIST",
                fontFamily = BangersFont,
                fontSize = 20.sp,
                color = MarvelGold
              )
              Spacer(modifier = Modifier.height(12.dp))

              GeneratorFieldBox(label = "INCITING HOOK", text = currentStory.hook, accentColor = MarvelGold)
              GeneratorFieldBox(label = "VILLAIN & SCHEME", text = currentStory.villainScheme, accentColor = MarvelRed)
              GeneratorFieldBox(label = "MCGUFFIN / OBJECTIVE", text = currentStory.mcGuffin, accentColor = FocusCyan)
              GeneratorFieldBox(label = "SCENE SETUP", text = currentStory.sceneSetup, accentColor = Color.LightGray)
              GeneratorFieldBox(label = "COMPLICATION", text = currentStory.complication, accentColor = Color(0xFFF59E0B))
              GeneratorFieldBox(label = "UNEXPECTED TWIST", text = currentStory.twist, accentColor = Color(0xFFA855F7))

              Spacer(modifier = Modifier.height(14.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Button(
                  onClick = { viewModel.regenerateStory() },
                  colors = ButtonDefaults.buttonColors(containerColor = MarvelRed),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("reroll_story_button")
                ) {
                  Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("REROLL STORY", fontFamily = BangersFont, fontSize = 14.sp)
                }

                Button(
                  onClick = {
                    viewModel.saveStoryToLog()
                    Toast.makeText(context, "Saved story to Session Log!", Toast.LENGTH_SHORT).show()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = ComicCardElevated, contentColor = MarvelGold),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("save_story_button")
                ) {
                  Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("SAVE TO NOTES", fontFamily = BangersFont, fontSize = 14.sp)
                }
              }
            }
          }
        }
      }

      1 -> {
        // NPC GENERATOR
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp)
              .border(1.5.dp, MarvelGold, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = ComicCard),
            shape = RoundedCornerShape(14.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = currentNpc.codename.uppercase(),
                    fontFamily = BangersFont,
                    fontSize = 24.sp,
                    color = MarvelGold
                  )
                  Text(
                    text = "${currentNpc.name} • Rank ${currentNpc.rank} ${currentNpc.archetype}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                  )
                }

                Box(
                  modifier = Modifier
                    .background(MarvelRed, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(text = currentNpc.origin.uppercase(), fontFamily = BangersFont, fontSize = 11.sp, color = Color.White)
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              GeneratorFieldBox(label = "MOTIVATION & GOAL", text = currentNpc.motivation, accentColor = MarvelGold)
              GeneratorFieldBox(label = "PERSONALITY QUIRK", text = currentNpc.personalityQuirk, accentColor = Color(0xFFF59E0B))
              GeneratorFieldBox(label = "SIGNATURE SUPER POWER", text = currentNpc.signaturePower, accentColor = FocusCyan)

              Spacer(modifier = Modifier.height(14.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Button(
                  onClick = { viewModel.regenerateNpc() },
                  colors = ButtonDefaults.buttonColors(containerColor = MarvelRed),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("reroll_npc_button")
                ) {
                  Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("REROLL NPC", fontFamily = BangersFont, fontSize = 14.sp)
                }

                Button(
                  onClick = {
                    viewModel.saveNpcToLog()
                    Toast.makeText(context, "Saved NPC to Session Log!", Toast.LENGTH_SHORT).show()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = ComicCardElevated, contentColor = MarvelGold),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("save_npc_button")
                ) {
                  Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("SAVE TO NOTES", fontFamily = BangersFont, fontSize = 14.sp)
                }
              }
            }
          }
        }
      }

      2 -> {
        // ENCOUNTER GENERATOR
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp)
              .border(1.5.dp, FocusCyan, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = ComicCard),
            shape = RoundedCornerShape(14.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "SUPER-HEROIC BATTLE ENCOUNTER",
                fontFamily = BangersFont,
                fontSize = 20.sp,
                color = MarvelGold
              )
              Spacer(modifier = Modifier.height(12.dp))

              GeneratorFieldBox(label = "HOSTILE FORCES", text = currentEncounter.enemies, accentColor = HealthRed)
              GeneratorFieldBox(label = "BATTLEFIELD & HAZARDS", text = currentEncounter.terrain, accentColor = FocusCyan)
              GeneratorFieldBox(label = "HIGH STAKES / CRISIS", text = currentEncounter.stakes, accentColor = MarvelGold)
              GeneratorFieldBox(label = "TACTICAL COMPLICATION", text = currentEncounter.complication, accentColor = Color(0xFFF59E0B))

              Spacer(modifier = Modifier.height(14.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Button(
                  onClick = { viewModel.regenerateEncounter() },
                  colors = ButtonDefaults.buttonColors(containerColor = MarvelRed),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("reroll_encounter_button")
                ) {
                  Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("REROLL BATTLE", fontFamily = BangersFont, fontSize = 14.sp)
                }

                Button(
                  onClick = {
                    viewModel.saveEncounterToLog()
                    Toast.makeText(context, "Saved Encounter to Session Log!", Toast.LENGTH_SHORT).show()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = ComicCardElevated, contentColor = MarvelGold),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("save_encounter_button")
                ) {
                  Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("SAVE TO NOTES", fontFamily = BangersFont, fontSize = 14.sp)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun GeneratorFieldBox(
  label: String,
  text: String,
  accentColor: Color
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
  ) {
    Text(
      text = label,
      fontFamily = BangersFont,
      fontSize = 13.sp,
      color = accentColor,
      letterSpacing = 0.5.sp
    )
    Surface(
      shape = RoundedCornerShape(8.dp),
      color = ComicCardElevated,
      modifier = Modifier
        .fillMaxWidth()
        .border(0.5.dp, ComicBorder, RoundedCornerShape(8.dp))
        .padding(10.dp)
    ) {
      Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = ComicTextPrimary,
        lineHeight = 19.sp
      )
    }
  }
}
