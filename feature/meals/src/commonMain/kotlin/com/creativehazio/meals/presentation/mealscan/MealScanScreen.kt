package com.creativehazio.meals.presentation.mealscan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.back_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun MealScanScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    viewModel: MealScanViewModel,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    Scaffold(
        modifier = Modifier.padding(paddingValues)
    ) { innerPadding ->
        MealScanScreen(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onEvent = event
        )
    }
}

@Composable
internal fun MealScanScreen(
    modifier: Modifier = Modifier,
    uiState: MealScanState,
    onEvent: (MealScanEvent) -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(Res.drawable.back_icon),
                    contentDescription = null,
                )
            }
            Text(
                text = "Scan ",
                style = MaterialTheme.typography.titleLarge
            )
            Box(Modifier.size(Sizing.IconLarge))

        }

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun MealScanPreview() {
    MealScanScreen(
        uiState = MealScanState(),
        onEvent = {}
    )
}