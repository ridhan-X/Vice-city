package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AyuGuardTheme(
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (isDark) DarkThemeColors else DayThemeColors

    CompositionLocalProvider(LocalAppColors provides colors) {
        content()
    }
}
