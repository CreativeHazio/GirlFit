package com.creativehazio.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.designsystem.theme.greyDisabledButtonLight
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    text: String,
    leadingIcon: DrawableResource? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier.height(Sizing.ButtonHeight),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        ),
        enabled = enabled && !isLoading,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularWavyProgressIndicator(
                    modifier = Modifier.size(Spacing.Large),
                    color = MaterialTheme.colorScheme.onPrimary,
                    stroke = Stroke(width = 2f)
                )
            } else {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

}