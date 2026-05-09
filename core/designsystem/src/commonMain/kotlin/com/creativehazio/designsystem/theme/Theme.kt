package com.creativehazio.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val lightColorTheme = lightColorScheme(
    primary = greenPrimaryLight,
    secondary = pinkAccentLight,
    background = greyBackgroundLight,
    onBackground = textSecondaryLight,
    surface = greyBackgroundLight,
    onSurfaceVariant = textLabelLight
)

@Composable
fun GirlFitTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorTheme,
        content = content,
        typography = girlFitAppTypography(),
        shapes = girlFitShapes,
    )
}