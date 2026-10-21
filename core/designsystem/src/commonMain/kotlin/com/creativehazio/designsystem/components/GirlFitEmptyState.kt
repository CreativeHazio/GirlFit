package com.creativehazio.designsystem.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import com.creativehazio.designsystem.theme.Spacing
import girlfit.core.designsystem.generated.resources.Res
import girlfit.core.designsystem.generated.resources.retry_ic
import org.jetbrains.compose.resources.painterResource

@Composable
fun GirlfitEmptyState(
    modifier: Modifier = Modifier,
    title: String = "Nothing to show here",
    subtitle: String? = null,
    retry: () -> Unit
) {
    var isRetrying by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "retrying")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        label = "rotation",
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(Spacing.Small))
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = Spacing.ExtraSmall)
            )
        }
        Spacer(Modifier.size(Spacing.Small))
        IconButton(
            enabled = !isRetrying,
            onClick = {
                isRetrying = true
                retry()
            }
        ) {
            Icon(
                modifier = Modifier.graphicsLayer {
                    rotationZ = if (isRetrying) rotation else 0f
                },
                painter = painterResource(Res.drawable.retry_ic),
                contentDescription = null,
            )
        }
    }
}