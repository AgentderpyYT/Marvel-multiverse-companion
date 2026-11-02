package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val BangersFont = FontFamily(Font(R.font.bangers, FontWeight.Normal))
val RubikFont = FontFamily(
  Font(R.font.rubik, FontWeight.Normal),
  Font(R.font.rubik, FontWeight.Bold)
)

val ComicTypography = Typography(
  displayLarge = TextStyle(
    fontFamily = BangersFont,
    fontWeight = FontWeight.Normal,
    fontSize = 38.sp,
    letterSpacing = 1.2.sp
  ),
  displayMedium = TextStyle(
    fontFamily = BangersFont,
    fontWeight = FontWeight.Normal,
    fontSize = 30.sp,
    letterSpacing = 1.sp
  ),
  displaySmall = TextStyle(
    fontFamily = BangersFont,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    letterSpacing = 0.8.sp
  ),
  headlineLarge = TextStyle(
    fontFamily = BangersFont,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    letterSpacing = 0.5.sp
  ),
  headlineMedium = TextStyle(
    fontFamily = BangersFont,
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    letterSpacing = 0.5.sp
  ),
  headlineSmall = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp
  ),
  titleLarge = TextStyle(
    fontFamily = BangersFont,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    letterSpacing = 0.5.sp
  ),
  titleMedium = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Bold,
    fontSize = 15.sp
  ),
  titleSmall = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Bold,
    fontSize = 13.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    lineHeight = 22.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    lineHeight = 18.sp
  ),
  bodySmall = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    lineHeight = 15.sp
  ),
  labelLarge = TextStyle(
    fontFamily = BangersFont,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.8.sp
  ),
  labelMedium = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp
  ),
  labelSmall = TextStyle(
    fontFamily = RubikFont,
    fontWeight = FontWeight.Medium,
    fontSize = 10.sp,
    letterSpacing = 0.5.sp
  )
)
