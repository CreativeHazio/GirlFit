package com.creativehazio.designsystem

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.modifyIf(condition: Boolean, block: Modifier.() -> Modifier): Modifier {
    return if (condition) this.then(block(Modifier)) else this
}

@Composable
fun Modifier.shimmer(
    baseColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    highlightColor: Color = MaterialTheme.colorScheme.surface,
    durationMillis: Int = 1200,
): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing)
        ),
        label = "shimmerProgress"
    )

    return drawWithCache {
        onDrawBehind {
            // `progress` is read here, in the draw phase, so the animation
            // doesn't trigger recomposition on every frame.
            val startX = -size.width + progress * 2 * size.width
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(baseColor, highlightColor, baseColor),
                    start = Offset(startX, 0f),
                    end = Offset(startX + size.width, 0f)
                )
            )
        }
    }
}