package com.creativehazio.designsystem.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.greyDisabledButtonLight
import girlfit.core.designsystem.generated.resources.Res
import girlfit.core.designsystem.generated.resources.close
import girlfit.core.designsystem.generated.resources.filter
import girlfit.core.designsystem.generated.resources.search
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    placeholderText: String,
    onSearchPressed: () -> Unit,
    showFilterIcon: Boolean = false,
    onFilterClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val isDark = isSystemInDarkTheme()

    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(Sizing.ButtonHeight),
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = greyDisabledButtonLight,
            unfocusedContainerColor = greyDisabledButtonLight,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodySmall,
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.search),
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(Res.drawable.close),
                        contentDescription = "Clear Text",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (showFilterIcon) {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        painter = painterResource(Res.drawable.filter),
                        contentDescription = "Filter Options",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = placeholderText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchPressed()
                focusManager.clearFocus()
            }
        ),
        singleLine = true
    )
}