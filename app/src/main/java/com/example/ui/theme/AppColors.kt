package com.example.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val bg: Color,
    val surface: Color,
    val card: Color,
    val cardHover: Color,
    val border: Color,
    val borderStrong: Color,
    val red: Color,
    val redDim: Color,
    val green: Color,
    val text1: Color,
    val text2: Color,
    val text3: Color,
    val blue: Color,
    val inputBg: Color
)

val DarkThemeColors = AppColors(
    bg = DarkBg, surface = DarkSurface, card = DarkCard, cardHover = DarkCardHover,
    border = DarkBorder, borderStrong = DarkBorderStrong, red = DarkRed, redDim = DarkRedDim,
    green = DarkGreen, text1 = DarkText1, text2 = DarkText2, text3 = DarkText3,
    blue = DarkBlue, inputBg = DarkInputBg
)

val DayThemeColors = AppColors(
    bg = DayBg, surface = DaySurface, card = DayCard, cardHover = DayCardHover,
    border = DayBorder, borderStrong = DayBorderStrong, red = DayRed, redDim = DayRedDim,
    green = DayGreen, text1 = DayText1, text2 = DayText2, text3 = DayText3,
    blue = DayBlue, inputBg = DayInputBg
)

val LocalAppColors = staticCompositionLocalOf { DarkThemeColors }
