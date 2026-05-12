package com.creativehazio.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.greyDisabledButtonLight
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    text: String,
    leadingIcon: DrawableResource? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier.height(Sizing.ButtonHeight),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = greyDisabledButtonLight,
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            leadingIcon?.let {
                Icon(
                    painter = painterResource(leadingIcon),
                    contentDescription = null
                )
            }
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

}