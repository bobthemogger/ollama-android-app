package com.example.ollamapp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val OllamaAppTheme: ComposableTheme = {

    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            header1 = TextStyle(
                fontWeight = androidx.compose.ui.font.weight.Semibold,
                fontSize = 24.sp,
                color = colorScheme.primary
            ),
            body1 = TextStyle(
                fontSize = 16.sp,
                color = colorScheme.onSurface
            )
        )
    )
}

@Composable
fun isSystemInDarkTheme(): Boolean {
    val context = LocalContext.current
    val insets = rememberWindowInsets { it }
    insets.systemBarsNavigationBarConfiguration.isLegacyDarkBars
}