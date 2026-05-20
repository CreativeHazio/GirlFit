package com.creativehazio.designsystem.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import girlfit.core.designsystem.generated.resources.Res
import girlfit.core.designsystem.generated.resources.filter_ic
import girlfit.core.designsystem.generated.resources.search_ic

@Composable
fun GirlFitSearchBar(
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

    GirlFitTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = Res.drawable.search_ic,
        trailingIcon = if (showFilterIcon) Res.drawable.filter_ic else null,
        onTrailingIconClick = onFilterClick,
        placeholderText = placeholderText,
        singleLine = true,
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
    )
}