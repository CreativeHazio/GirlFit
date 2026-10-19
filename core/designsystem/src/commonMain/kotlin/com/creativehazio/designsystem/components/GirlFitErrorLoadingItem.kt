package com.creativehazio.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import girlfit.core.designsystem.generated.resources.Res
import girlfit.core.designsystem.generated.resources.love_emoji
import org.jetbrains.compose.resources.painterResource

@Composable
fun GirlfitErrorLoadingItem(
    error: String?,
    onRetryClicked: () -> Unit,
    modifier: Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error ?: "Retry"
        )
        IconButton(onClick = onRetryClicked) {
            Icon(
                painter = painterResource(Res.drawable.love_emoji),
                contentDescription = null
            )
        }
    }

}