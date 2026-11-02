package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BangersFont
import com.example.ui.theme.DieMarvelGold
import com.example.ui.theme.DieMarvelRed
import com.example.ui.theme.DieStandardDot
import com.example.ui.theme.DieStandardWhite
import com.example.ui.theme.FantasticGlow
import com.example.ui.theme.MarvelDarkRed
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed

@Composable
fun D6DieView(
  value: Int,
  isMarvelDie: Boolean = false,
  isSelectedForReroll: Boolean = false,
  size: Dp = if (isMarvelDie) 90.dp else 76.dp,
  onClick: (() -> Unit)? = null,
  isRolling: Boolean = false,
  testTag: String = "die_view"
) {
  val rotation = remember { Animatable(0f) }

  LaunchedEffect(isRolling, value) {
    if (isRolling) {
      val targetRot = if (isMarvelDie) 720f else 360f
      val duration = if (isMarvelDie) 380 else 240
      rotation.animateTo(
        targetValue = targetRot,
        animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing)
      )
      rotation.snapTo(0f)
    }
  }

  val isMarvelOne = isMarvelDie && value == 1

  // Pulsing glow animation for Marvel 1
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
  val pulseScale by if (isMarvelOne) {
    infiniteTransition.animateFloat(
      initialValue = 1f,
      targetValue = 1.08f,
      animationSpec = infiniteRepeatable(
        animation = tween(600, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Reverse
      ),
      label = "pulse_scale"
    )
  } else {
    remember { Animatable(1f) }.asState()
  }

  val bgBrush = when {
    isMarvelOne -> Brush.radialGradient(
      listOf(Color(0xFFFDE047), FantasticGlow, DieMarvelRed, Color(0xFF450A0A))
    )
    isMarvelDie -> Brush.verticalGradient(
      listOf(DieMarvelRed, MarvelDarkRed)
    )
    else -> Brush.verticalGradient(
      listOf(DieStandardWhite, Color(0xFFE2E8F0))
    )
  }

  val borderColor = when {
    isSelectedForReroll -> MarvelGold
    isMarvelOne -> Color.White
    isMarvelDie -> DieMarvelGold
    else -> Color(0xFF94A3B8)
  }

  val borderWidth = if (isSelectedForReroll || isMarvelOne) 3.5.dp else 2.dp

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(size)
        .scale(pulseScale)
        .rotate(rotation.value)
        .shadow(
          elevation = if (isMarvelOne) 16.dp else if (isMarvelDie) 10.dp else 6.dp,
          shape = RoundedCornerShape(16.dp),
          ambientColor = if (isMarvelDie) MarvelRed else Color.Black,
          spotColor = if (isMarvelOne) FantasticGlow else if (isMarvelDie) MarvelRed else Color.Black
        )
        .clip(RoundedCornerShape(16.dp))
        .background(bgBrush)
        .border(borderWidth, borderColor, RoundedCornerShape(16.dp))
        .then(
          if (onClick != null) {
            Modifier.clickable { onClick() }
          } else Modifier
        )
        .testTag(testTag),
      contentAlignment = Alignment.Center
    ) {
      if (isMarvelOne) {
        // Marvel Symbol / Fantastic Roll with distinctive 'M'
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = "M",
            fontFamily = BangersFont,
            fontSize = (size.value * 0.46f).sp,
            color = MarvelGold,
            fontWeight = FontWeight.Bold
          )
          Box(
            modifier = Modifier
              .background(MarvelGold, RoundedCornerShape(4.dp))
              .padding(horizontal = 4.dp, vertical = 1.dp)
          ) {
            Text(
              text = "COUNTS AS 6",
              fontSize = 8.sp,
              fontFamily = BangersFont,
              color = Color.Black
            )
          }
        }
      } else {
        // Standard D6 Pips
        val dotColor = if (isMarvelDie) MarvelGold else DieStandardDot
        DicePips(value = value, dotColor = dotColor, dieSize = size)
      }
    }

    Spacer(modifier = Modifier.height(4.dp))

    if (isMarvelDie) {
      Box(
        modifier = Modifier
          .background(if (isMarvelOne) FantasticGlow else MarvelRed, RoundedCornerShape(4.dp))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = if (isMarvelOne) "MARVEL 1 (6)" else "MARVEL DIE",
          fontFamily = BangersFont,
          fontSize = 10.sp,
          color = if (isMarvelOne) Color.Black else Color.White
        )
      }
    } else {
      Text(
        text = "D6 ($value)",
        fontFamily = BangersFont,
        fontSize = 11.sp,
        color = Color.LightGray
      )
    }

    if (isSelectedForReroll) {
      Text(
        text = "SELECTED",
        fontFamily = BangersFont,
        fontSize = 10.sp,
        color = MarvelGold
      )
    }
  }
}

@Composable
private fun DicePips(
  value: Int,
  dotColor: Color,
  dieSize: Dp
) {
  val pipSize = (dieSize.value * 0.16f).dp
  val p = (dieSize.value * 0.14f).dp

  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(p)
  ) {
    when (value) {
      1 -> {
        Box(
          modifier = Modifier
            .size(pipSize * 1.3f)
            .align(Alignment.Center)
            .background(dotColor, CircleShape)
        )
      }
      2 -> {
        Box(modifier = Modifier.size(pipSize).align(Alignment.TopStart).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.BottomEnd).background(dotColor, CircleShape))
      }
      3 -> {
        Box(modifier = Modifier.size(pipSize).align(Alignment.TopStart).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.Center).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.BottomEnd).background(dotColor, CircleShape))
      }
      4 -> {
        Box(modifier = Modifier.size(pipSize).align(Alignment.TopStart).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.TopEnd).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.BottomStart).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.BottomEnd).background(dotColor, CircleShape))
      }
      5 -> {
        Box(modifier = Modifier.size(pipSize).align(Alignment.TopStart).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.TopEnd).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.Center).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.BottomStart).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(pipSize).align(Alignment.BottomEnd).background(dotColor, CircleShape))
      }
      6 -> {
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            modifier = Modifier.fillMaxSize().weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(modifier = Modifier.size(pipSize).background(dotColor, CircleShape))
            Box(modifier = Modifier.size(pipSize).background(dotColor, CircleShape))
          }
          Row(
            modifier = Modifier.fillMaxSize().weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(modifier = Modifier.size(pipSize).background(dotColor, CircleShape))
            Box(modifier = Modifier.size(pipSize).background(dotColor, CircleShape))
          }
          Row(
            modifier = Modifier.fillMaxSize().weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(modifier = Modifier.size(pipSize).background(dotColor, CircleShape))
            Box(modifier = Modifier.size(pipSize).background(dotColor, CircleShape))
          }
        }
      }
    }
  }
}
