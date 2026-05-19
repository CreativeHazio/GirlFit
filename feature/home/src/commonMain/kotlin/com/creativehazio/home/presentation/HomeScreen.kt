package com.creativehazio.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.home.domain.RecommendedWorkout
import com.creativehazio.home.domain.RelaxWorkout
import girlfit.feature.home.generated.resources.Res
import girlfit.feature.home.generated.resources.angry_emoji
import girlfit.feature.home.generated.resources.exhausted_emoji
import girlfit.feature.home.generated.resources.lollipop
import girlfit.feature.home.generated.resources.sad_emoji
import girlfit.feature.home.generated.resources.sick_emoji
import girlfit.feature.home.generated.resources.smile_emoji
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreenRoot(
    homeViewModel: HomeViewModel,
    contentPaddingValues: PaddingValues,
    onNavigateToWorkoutDetail: (String) -> Unit
) {
    // TODO: Create a script to generate this boilerplate and also viewmodel boilerplate
    val uiState = homeViewModel.uiState.collectAsStateWithLifecycle().value
    val event = homeViewModel::onEvent

    LaunchedEffect(homeViewModel.effect) {

    }

    HomeScreen(
        contentPaddingValues = contentPaddingValues,
        uiState = uiState,
        event = event,
    )

}

@Composable
internal fun HomeScreen(
    contentPaddingValues: PaddingValues,
    uiState: HomeState,
    event: (HomeEvent) -> Unit
) {

    LazyColumn(
        modifier = Modifier.padding(
            top = Spacing.Medium,
            start = Spacing.Medium,
            end = Spacing.Medium
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.ExtraLarge),
        contentPadding = contentPaddingValues
    ) {

        item {
            HomeScreenWelcomeSection()
        }

        item {
            HomeScreenCycleCalenderSection()
        }

        item {
            val recommendedWorkouts = remember {
                mutableStateListOf(
                    RecommendedWorkout(
                        title = "Full body workout",
                        durationText = "15 mins",
                        imageUrl = ""
                    ),
                    RecommendedWorkout(
                        title = "Full body stretch",
                        durationText = "12 mins",
                        imageUrl = ""
                    ),
                )
            }
            RecommendedWorkoutSection(
                recommendedWorkouts = recommendedWorkouts
            )
        }

        item {
            val relaxWorkouts = remember {
                mutableStateListOf(
                    RelaxWorkout(
                        title = "De-stress",
                        durationText = "15 mins",
                        imageUrl = ""
                    )
                )
            }
            RelaxWorkoutSection(
                relaxWorkouts = relaxWorkouts
            )
            Spacer(Modifier.size(Spacing.Small))
        }

    }

}

@Composable
internal fun HomeScreenWelcomeSection() {
    // TODO: Create a data class?
    val feelings = remember {
        mutableStateMapOf(
            "Happy" to Res.drawable.smile_emoji,
            "Sad" to Res.drawable.sad_emoji,
            "Sick" to Res.drawable.sick_emoji,
            "Tired" to Res.drawable.exhausted_emoji,
            "Angry" to Res.drawable.angry_emoji,
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Text("Hi Ria ❤️")
                Text("How do you feel today?")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
            ) {
                Image(
                    modifier = Modifier.size(Sizing.IconLarge),
                    painter = painterResource(Res.drawable.lollipop),
                    contentDescription = null
                )
                Text("6")
            }

        }

        Spacer(Modifier.size(Spacing.Large))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            feelings.forEach { resource ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
                ) {
                    Image(
                        modifier = Modifier.size(Sizing.IconLarge),
                        painter = painterResource(resource.value),
                        contentDescription = null
                    )
                    Text(resource.key)
                }
            }
        }

    }
}

@Composable
internal fun HomeScreenCycleCalenderSection() {
    Column {
        Row {
            Text("14th day of Cycle")
        }
        Spacer(Modifier.size(Spacing.Medium))
        CycleCalender()
        Spacer(Modifier.size(Spacing.Medium))
        GirlFitInfoBubble(
            text = "Hormonal shift may lead to mood\n" +
                    "swings, bloating & fatigue"
        )
    }
}

@Composable
internal fun CycleCalender() {
    val daysOfTheWeek = remember {
        mutableStateListOf(
            "Sun", "Mon", "Tue", "Wed", "Thurs", "Fri", "Sat"
        )
    }

    val thisWeekDay = remember {
        mutableStateListOf(
            "30", "01", "02", "03", "04", "05", "06"
        )
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Medium)
        ) {
            Text("2 December")
            Spacer(Modifier.size(Spacing.Medium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfTheWeek.forEachIndexed { index, day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(day)
                        Spacer(Modifier.size(Spacing.Small))
                        Text(
                            thisWeekDay[index], modifier = Modifier.background(
                                color = if (index == 3) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            ).padding(Spacing.ExtraSmall)
                        )
                    }
                }
            }

        }
    }
}

@Composable
internal fun RecommendedWorkoutSection(
    recommendedWorkouts: List<RecommendedWorkout>
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Text("Recommended workouts")
        Spacer(Modifier.size(Spacing.ExtraSmall))
        recommendedWorkouts.forEach { workout ->
            HomeScreenWorkoutCard(
                title = workout.title,
                durationText = workout.durationText,
                imageUrl = workout.imageUrl
            )
        }
    }
}

@Composable
fun HomeScreenWorkoutCard(
    title: String,
    imageUrl: String,
    detailsText: String? = null,
    durationText: String,
) {

    Card(
        modifier = Modifier.fillMaxWidth().height(120.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
            )

            Column(
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(Spacing.Medium)
            ) {
                Text(title)
                Spacer(Modifier.size(Spacing.Small))
                Row {
                    Text(durationText)
                }
            }
        }
    }

}

@Composable
internal fun RelaxWorkoutSection(
    relaxWorkouts: List<RelaxWorkout>
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Text("Relax instead?")
        Spacer(Modifier.size(Spacing.ExtraSmall))
        relaxWorkouts.forEach { workout ->
            HomeScreenWorkoutCard(
                title = workout.title,
                durationText = workout.durationText,
                imageUrl = workout.imageUrl
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        contentPaddingValues = PaddingValues.Zero,
        uiState = HomeState(),
        event = {}
    )
}