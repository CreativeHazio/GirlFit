package com.creativehazio.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.designsystem.theme.greyDisabledButtonLight
import girlfit.core.designsystem.generated.resources.Res
import girlfit.core.designsystem.generated.resources.close_ic
import girlfit.core.designsystem.generated.resources.forgot_password
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String? = null,
    placeholderText: String = "",
    isError: Boolean = false,
    errorText: String = "",
    singleLine: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: DrawableResource? = null,
    trailingIcon: DrawableResource? = null,
    onTrailingIconClick: () -> Unit = {},
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = greyDisabledButtonLight,
            unfocusedContainerColor = greyDisabledButtonLight,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            errorIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    modifier = Modifier.size(Sizing.IconMedium),
                    painter = painterResource(it),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                AnimatedVisibility(
                    value.isNotEmpty(),
                    enter = slideInHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 500)
                    ) + expandHorizontally(
                        animationSpec = tween(durationMillis = 500)
                    ),
                    exit = slideOutHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        targetOffsetX = { fullWidth -> fullWidth }
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 500)
                    ) + shrinkHorizontally(
                        animationSpec = tween(durationMillis = 500)
                    )
                ) {
                    val rotation by transition.animateFloat(
                        transitionSpec = { tween(durationMillis = 500) },
                        label = "RollAnimation"
                    ) { state ->
                        when (state) {
                            EnterExitState.PreEnter -> 180f
                            EnterExitState.Visible -> 0f
                            EnterExitState.PostExit -> -180f
                        }
                    }

                    IconButton(
                        onClick = { onValueChange("") },
                        modifier = Modifier.graphicsLayer {
                            rotationZ = rotation
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(Sizing.IconSmall),
                            painter = painterResource(Res.drawable.close_ic),
                            contentDescription = "Clear Text",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                trailingIcon?.let {
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(
                            modifier = Modifier.size(Sizing.IconMedium),
                            painter = painterResource(trailingIcon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        },
        label = labelText?.let{
            {
                Text(
                    text = labelText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        },
        placeholder = labelText.isNullOrBlank().let {
            {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        },
        isError = isError,
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isError) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = errorText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }

                if (visualTransformation == PasswordVisualTransformation()) {
                    Text(
                        text = stringResource(Res.string.forgot_password),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .clickable {
                                // TODO: Handle Forgot Password click
                            }
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                    )
                }
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        singleLine = singleLine
    )
}