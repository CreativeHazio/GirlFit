package com.creativehazio.designsystem

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionDefaults
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier


val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

data class SharedElementKey(val id: String, val type: Type) {
    enum class Type { Image, Title, Subtitle, Container }
}

@Composable
fun Modifier.sharedElementIfAvailable(
    key: SharedElementKey,
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform
): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    val animatedScope = LocalAnimatedVisibilityScope.current ?: return this
    return with(sharedScope) {
        this@sharedElementIfAvailable.sharedElement(
            sharedContentState = rememberSharedContentState(key),
            animatedVisibilityScope = animatedScope,
            boundsTransform = boundsTransform,
        )
    }
}

@Composable
fun Modifier.sharedBoundsIfAvailable(
    key: SharedElementKey,
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform
): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    val animatedScope = LocalAnimatedVisibilityScope.current ?: return this
    return with(sharedScope) {
        this@sharedBoundsIfAvailable.sharedBounds(
            sharedContentState = rememberSharedContentState(key),
            animatedVisibilityScope = animatedScope,
            boundsTransform = boundsTransform,
            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
        )
    }
}

@Composable
fun Modifier.renderAboveSharedElements(): Modifier {
    val sharedScope = LocalSharedTransitionScope.current
        ?: return this

    val animatedScope = LocalAnimatedVisibilityScope.current
        ?: return this

    return with(sharedScope) {
        val overlayModifier = this@renderAboveSharedElements
            .renderInSharedTransitionScopeOverlay(
                zIndexInOverlay = 1f
            )

        with(animatedScope) {
            overlayModifier.animateEnterExit(
                exit = fadeOut(
                    animationSpec = tween(200)
                ) + slideOutVertically(
                    animationSpec = tween(200)
                ) { fullHeight -> fullHeight }
            )
        }
    }
}