package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.RollOutcome
import com.example.ui.theme.BangersFont
import com.example.ui.theme.ComicBorder
import com.example.ui.theme.ComicCard
import com.example.ui.theme.ComicDarkBg
import com.example.ui.theme.FantasticGlow
import com.example.ui.theme.HealthGreen
import com.example.ui.theme.HealthRed
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed

@Composable
fun ComicHeaderBanner(
  title: String,
  subtitle: String? = null,
  modifier: Modifier = Modifier,
  badgeText: String? = null
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp, vertical = 6.dp)
  ) {
    Surface(
      shape = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp),
      color = ComicCard,
      modifier = Modifier
        .fillMaxWidth()
        .border(2.dp, MarvelRed, CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
        .shadow(4.dp, CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = title.uppercase(),
            fontFamily = BangersFont,
            fontSize = 24.sp,
            color = MarvelGold,
            letterSpacing = 1.sp
          )
          if (!subtitle.isNullOrBlank()) {
            Text(
              text = subtitle,
              style = MaterialTheme.typography.bodySmall,
              color = Color.LightGray
            )
          }
        }

        if (!badgeText.isNullOrBlank()) {
          Box(
            modifier = Modifier
              .background(MarvelRed, RoundedCornerShape(6.dp))
              .border(1.dp, MarvelGold, RoundedCornerShape(6.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = badgeText,
              fontFamily = BangersFont,
              fontSize = 13.sp,
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@Composable
fun OutcomeBadge(
  outcome: RollOutcome,
  modifier: Modifier = Modifier
) {
  val (bgBrush, textColor) = when (outcome) {
    RollOutcome.ULTIMATE_FANTASTIC -> Pair(
      Brush.horizontalGradient(listOf(MarvelGold, MarvelRed, MarvelGold)),
      ComicDarkBg
    )
    RollOutcome.FANTASTIC_SUCCESS -> Pair(
      Brush.horizontalGradient(listOf(Color(0xFF059669), Color(0xFF10B981))),
      Color.White
    )
    RollOutcome.FANTASTIC_FAILURE -> Pair(
      Brush.horizontalGradient(listOf(Color(0xFFD97706), Color(0xFFF59E0B))),
      ComicDarkBg
    )
    RollOutcome.SUCCESS -> Pair(
      Brush.horizontalGradient(listOf(Color(0xFF0284C7), Color(0xFF00E5FF))),
      ComicDarkBg
    )
    RollOutcome.FAILURE -> Pair(
      Brush.horizontalGradient(listOf(Color(0xFF991B1B), HealthRed)),
      Color.White
    )
    RollOutcome.CHECK -> Pair(
      Brush.horizontalGradient(listOf(Color(0xFF374151), Color(0xFF4B5563))),
      Color.White
    )
  }

  Box(
    modifier = modifier
      .shadow(6.dp, RoundedCornerShape(8.dp))
      .background(bgBrush, RoundedCornerShape(8.dp))
      .border(1.5.dp, if (outcome == RollOutcome.ULTIMATE_FANTASTIC) Color.White else MarvelGold, RoundedCornerShape(8.dp))
      .padding(horizontal = 14.dp, vertical = 6.dp)
      .testTag("outcome_badge_${outcome.name}")
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (outcome == RollOutcome.ULTIMATE_FANTASTIC || outcome == RollOutcome.FANTASTIC_SUCCESS) {
        Icon(
          imageVector = Icons.Default.Star,
          contentDescription = null,
          tint = if (outcome == RollOutcome.ULTIMATE_FANTASTIC) ComicDarkBg else MarvelGold,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
      }
      Text(
        text = outcome.title,
        fontFamily = BangersFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        letterSpacing = 0.5.sp
      )
    }
  }
}
