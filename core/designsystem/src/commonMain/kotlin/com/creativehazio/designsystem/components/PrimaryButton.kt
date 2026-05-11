package com.creativehazio.designsystem.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.creativehazio.designsystem.theme.Sizing

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier.height(Sizing.ButtonHeight),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge
        )
    }

}