package com.example.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BangersFont
import com.example.ui.theme.ComicBorder
import com.example.ui.theme.ComicCard
import com.example.ui.theme.ComicCardElevated
import com.example.ui.theme.ComicDarkBg
import com.example.ui.theme.ComicTextPrimary
import com.example.ui.theme.ComicTextSecondary
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed

enum class ComicThemePreset(
  val label: String,
  val primaryColor: Color,
  val accentColor: Color,
  val bgColor: Color,
  val description: String
) {
  CLASSIC_COMIC(
    "Classic Comic",
    MarvelRed,
    MarvelGold,
    ComicDarkBg,
    "Bold primary Marvel red, golden yellow, high contrast comic styling."
  ),
  COSMIC_MARVEL(
    "Cosmic Marvel",
    Color(0xFF8B5CF6),
    Color(0xFF00E5FF),
    Color(0xFF0B0819),
    "Deep galactic purples, electric cyan, and supernova gold."
  ),
  GRITTY_NOIR(
    "Gritty Noir",
    Color(0xFFDC2626),
    Color(0xFFE5E7EB),
    Color(0xFF08090A),
    "Muted grays, near-black alley shadows, single restrained crimson accent."
  )
}

@Composable
fun ThemeCustomizerDialog(
  currentPreset: ComicThemePreset,
  onSelectPreset: (ComicThemePreset) -> Unit,
  onDismiss: () -> Unit
) {
  var selected by remember { mutableStateOf(currentPreset) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.Palette, contentDescription = null, tint = MarvelGold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "THEME CUSTOMIZATION",
          fontFamily = BangersFont,
          fontSize = 20.sp,
          color = MarvelGold
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "SELECT COLOR PALETTE",
          fontFamily = BangersFont,
          fontSize = 14.sp,
          color = Color.LightGray
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          ComicThemePreset.values().forEach { preset ->
            val isCurrent = selected == preset
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isCurrent) preset.primaryColor else ComicCardElevated)
                .border(2.dp, if (isCurrent) preset.accentColor else ComicBorder, RoundedCornerShape(8.dp))
                .clickable { selected = preset }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = preset.label.uppercase(),
                fontFamily = BangersFont,
                fontSize = 11.sp,
                color = if (isCurrent) Color.White else Color.Gray
              )
            }
          }
        }

        // Live Preview Box
        Text(
          text = "LIVE PREVIEW",
          fontFamily = BangersFont,
          fontSize = 13.sp,
          color = Color.LightGray
        )

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = selected.bgColor,
          modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, selected.primaryColor, RoundedCornerShape(10.dp))
            .padding(12.dp)
        ) {
          Column {
            Text(
              text = "${selected.label.uppercase()} PREVIEW",
              fontFamily = BangersFont,
              fontSize = 18.sp,
              color = selected.accentColor
            )
            Text(
              text = selected.description,
              style = MaterialTheme.typography.bodySmall,
              color = Color.LightGray,
              fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = {},
              colors = ButtonDefaults.buttonColors(
                containerColor = selected.primaryColor,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("SAMPLE ACTION BUTTON", fontFamily = BangersFont, fontSize = 13.sp)
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onSelectPreset(selected)
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
      ) {
        Text("APPLY THEME", fontFamily = BangersFont)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("CANCEL", color = Color.Gray, fontFamily = BangersFont)
      }
    },
    containerColor = ComicCard
  )
}
