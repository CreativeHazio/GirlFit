package com.creativehazio.workout.presentation.workoutdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import girlfit.feature.workout.generated.resources.Res
import girlfit.feature.workout.generated.resources.add_friend_icon
import girlfit.feature.workout.generated.resources.back_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkoutDetailScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    workoutViewModel: WorkoutDetailViewModel,
    onBack: () -> Unit
) {
    val uiState = workoutViewModel.uiState.collectAsStateWithLifecycle().value
    val event = workoutViewModel::onEvent

    WorkoutDetailScreen(
        modifier = Modifier.padding(paddingValues),
            onBack = onBack
    )
}

@Composable
internal fun WorkoutDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(350.dp)
        ) {
            AsyncImage(
                model = "",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(Res.drawable.back_icon),
                        contentDescription = null,
                    )
                }

                IconButton(onClick = { /* Handle action */ }) {
                    Icon(
                        painter = painterResource(Res.drawable.add_friend_icon),
                        contentDescription = null,
                    )
                }
            }

        }


        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Workout Title", style = MaterialTheme.typography.headlineMedium)
        }
    }

}

@Preview
@Composable
fun WorkoutDetailPreview() {
    WorkoutDetailScreen(
        onBack = {}
    )
}