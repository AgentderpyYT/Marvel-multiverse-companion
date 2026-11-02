package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CompanionViewModel
import com.example.ui.components.ComicThemePreset
import com.example.ui.components.QuickRulesOverlay
import com.example.ui.components.ThemeCustomizerDialog
import com.example.ui.screens.CharacterSheetsScreen
import com.example.ui.screens.DiceRollerScreen
import com.example.ui.screens.GeneratorsScreen
import com.example.ui.screens.SessionLogScreen
import com.example.ui.screens.SourcebookScreen
import com.example.ui.screens.WebViewCompanionScreen
import com.example.ui.theme.BangersFont
import com.example.ui.theme.ComicBorder
import com.example.ui.theme.ComicDarkBg
import com.example.ui.theme.ComicSurface
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed
import com.example.ui.theme.MultiverseTheme

enum class AppTab(val title: String, val icon: ImageVector) {
  ROLLER("ROLLER", Icons.Default.Casino),
  HERO("HERO", Icons.Default.Person),
  RULES("RULES", Icons.Default.Book),
  GENS("GENS", Icons.Default.AutoAwesome),
  LOGS("LOGS", Icons.Default.HistoryEdu)
}

class MainActivity : ComponentActivity() {

  private val viewModel: CompanionViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MultiverseTheme {
        MainAppContent(viewModel = viewModel)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: CompanionViewModel) {
  var currentTabIndex by rememberSaveable { mutableIntStateOf(0) }
  var isShowingWebView by rememberSaveable { mutableStateOf(false) }
  var isShowingQuickRules by rememberSaveable { mutableStateOf(false) }
  var isShowingThemeDialog by rememberSaveable { mutableStateOf(false) }
  var activeThemePreset by remember { mutableStateOf(ComicThemePreset.CLASSIC_COMIC) }

  if (isShowingQuickRules) {
    QuickRulesOverlay(onDismiss = { isShowingQuickRules = false })
  }

  if (isShowingThemeDialog) {
    ThemeCustomizerDialog(
      currentPreset = activeThemePreset,
      onSelectPreset = { activeThemePreset = it },
      onDismiss = { isShowingThemeDialog = false }
    )
  }

  if (isShowingWebView) {
    WebViewCompanionScreen(
      onBack = { isShowingWebView = false }
    )
  } else {
    Scaffold(
      modifier = Modifier
        .fillMaxSize()
        .background(activeThemePreset.bgColor),
      topBar = {
        TopAppBar(
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "D616 MULTIVERSE",
                fontFamily = BangersFont,
                fontSize = 22.sp,
                color = activeThemePreset.accentColor,
                letterSpacing = 1.sp
              )
            }
          },
          actions = {
            IconButton(
              onClick = { isShowingWebView = true },
              modifier = Modifier.testTag("top_web_suite_button")
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Full Web Companion Suite",
                tint = MarvelGold
              )
            }
            IconButton(
              onClick = { isShowingQuickRules = true },
              modifier = Modifier.testTag("top_quick_rules_button")
            ) {
              Icon(
                imageVector = Icons.Default.Book,
                contentDescription = "Quick Rules",
                tint = activeThemePreset.accentColor
              )
            }
            IconButton(
              onClick = { isShowingThemeDialog = true },
              modifier = Modifier.testTag("top_theme_button")
            ) {
              Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = "Themes",
                tint = Color.White
              )
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ComicSurface
          ),
          modifier = Modifier.border(0.5.dp, ComicBorder)
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = { isShowingQuickRules = true },
          containerColor = activeThemePreset.accentColor,
          contentColor = Color.Black,
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier.testTag("fab_quick_rules")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(imageVector = Icons.Default.FlashOn, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "RULES",
              fontFamily = BangersFont,
              fontSize = 14.sp
            )
          }
        }
      },
      bottomBar = {
        NavigationBar(
          containerColor = ComicSurface,
          modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .border(1.dp, ComicBorder)
            .testTag("main_bottom_nav")
        ) {
          AppTab.values().forEachIndexed { index, tab ->
            val isSelected = currentTabIndex == index
            NavigationBarItem(
              selected = isSelected,
              onClick = { currentTabIndex = index },
              icon = {
                Icon(
                  imageVector = tab.icon,
                  contentDescription = tab.title,
                  tint = if (isSelected) activeThemePreset.accentColor else Color.Gray
                )
              },
              label = {
                Text(
                  text = tab.title,
                  fontFamily = BangersFont,
                  fontSize = 12.sp,
                  color = if (isSelected) activeThemePreset.accentColor else Color.Gray,
                  letterSpacing = 0.5.sp
                )
              },
              colors = NavigationBarItemDefaults.colors(
                indicatorColor = activeThemePreset.primaryColor.copy(alpha = 0.3f),
                selectedIconColor = activeThemePreset.accentColor,
                unselectedIconColor = Color.Gray
              ),
              modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
          }
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        AnimatedContent(
          targetState = currentTabIndex,
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          label = "screen_transition"
        ) { tabIndex ->
          when (tabIndex) {
            0 -> DiceRollerScreen(viewModel = viewModel)
            1 -> CharacterSheetsScreen(
              viewModel = viewModel,
              onRollAbility = { abilityName, score ->
                viewModel.rollForAbility(abilityName, score)
                currentTabIndex = 0 // Switch to dice roller
              }
            )
            2 -> SourcebookScreen(viewModel = viewModel)
            3 -> GeneratorsScreen(viewModel = viewModel)
            4 -> SessionLogScreen(
              viewModel = viewModel,
              onOpenWebView = { isShowingWebView = true }
            )
          }
        }
      }
    }
  }
}
