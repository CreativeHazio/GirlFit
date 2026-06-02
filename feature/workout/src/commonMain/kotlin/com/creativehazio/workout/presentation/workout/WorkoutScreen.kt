package com.creativehazio.workout.presentation.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults.overscrollEffect
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.Card
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.creativehazio.common.util.DateTimeUtil
import com.creativehazio.data.workout.domain.ChallengeDayState
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutType
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.components.GirlFitSearchBar
import com.creativehazio.designsystem.components.GirlFitWorkoutCard
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.workout.generated.resources.Res
import girlfit.feature.workout.generated.resources.heart_icon_selected
import girlfit.feature.workout.generated.resources.premium_icon
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkoutScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    viewModel: WorkoutViewModel,
    onNavigateToFavourite: () -> Unit,
    onNavigateToPersonalPlan: () -> Unit,
    onNavigateToWorkoutDetail: (String) -> Unit,
    onNavigateToWorkoutChallengeCalender: (String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when (it) {
                WorkoutEffect.NavigateToFavourite -> onNavigateToFavourite()
                WorkoutEffect.NavigateToPersonalPlan -> onNavigateToPersonalPlan()
                is WorkoutEffect.NavigateToWorkoutDetail -> {
                    onNavigateToWorkoutDetail(it.workoutId)
                }

                is WorkoutEffect.NavigateToWorkoutChallengeCalender -> {
                    onNavigateToWorkoutChallengeCalender(it.workoutId)
                }
            }
        }
    }

    WorkoutScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        event = event
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun WorkoutScreen(
    paddingValues: PaddingValues,
    uiState: WorkoutState,
    event: (WorkoutEvent) -> Unit,
) {

    val workouts = uiState.workouts.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(horizontal = Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        contentPadding = paddingValues,
    ) {

        item(
            span = { GridItemSpan(maxLineSpan) },
            contentType = "welcome_and_search"
        ) {
            Spacer(Modifier.size(Spacing.Medium))
            WelcomeAndSearchSection(
                onSearchWorkoutClicked = {
                    event(WorkoutEvent.OnSearchWorkoutClicked(it))
                }
            )
        }

        item(
            span = { GridItemSpan(maxLineSpan) },
            contentType = "personal_plan"
        ) {
            PersonalPlanSection(
                onPersonalPlanCardClicked = {},
                onFavouriteInfoBubbleClicked = {}
            )
        }

        item(
            span = { GridItemSpan(maxLineSpan) },
            contentType = "goal_header_and_pill"
        ) {
            GoalHeaderAndPills(
                workoutCategory = uiState.workoutCategory,
                onWorkoutCategoryPillClicked = { event(WorkoutEvent.OnWorkoutCategoryPillClicked(it)) }
            )
        }

        items(
            count = workouts.itemCount,
            key = workouts.itemKey { it.id },
            contentType = { "workout_section" }
        ) { index ->

            val workout = workouts[index]

            if (workout != null) {
                val currentDay = workout.challenge.challengeDays.find {
                    it.state == ChallengeDayState.CURRENT
                }

                GirlFitWorkoutCard(
                    modifier = Modifier.height(Sizing.CardHeightLarge),
                    title = workout.title,
                    imageUrl = workout.imageUrl,
                    durationText = if (workout.duration == 0) null else DateTimeUtil.durationFormatter(
                        workout.duration
                    ),
                    detailsText = if (workout.type == WorkoutType.CHALLENGE) "${workout.challenge.challengeTitle} Challenge" else null,
                    buttonText = if (workout.type == WorkoutType.CHALLENGE) "Day ${currentDay?.number} 👏" else "Start",
                    onCardClick = { event(WorkoutEvent.OnWorkoutCardClicked(workout)) }
                )
            }

        }

    }

}

@Composable
internal fun WelcomeAndSearchSection(
    onSearchWorkoutClicked: (String) -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Text(
            "Let's go, Ria 💌",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        GirlFitSearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
            },
            placeholderText = "e.g., postpartum workout",
            onSearchPressed = {
                onSearchWorkoutClicked(searchQuery)
            }
        )
    }
}

@Composable
internal fun PersonalPlanSection(
    onPersonalPlanCardClicked: (String) -> Unit,
    onFavouriteInfoBubbleClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Text(
            "Personal plan 💥",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Card(
            modifier = Modifier.height(Sizing.CardHeightMedium).clickable {
                onPersonalPlanCardClicked("planId")
            },
            shape = MaterialTheme.shapes.medium
        ) {
            Box(Modifier.fillMaxSize()) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1733744577444-ecfd9d9110cc?q=80&w=2531&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
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

                Row(
                    modifier = Modifier.fillMaxWidth().padding(Spacing.Medium),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Workouts\nmade just for you ❤️",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Image(
                        modifier = Modifier.size(Sizing.IconMedium),
                        painter = painterResource(Res.drawable.premium_icon),
                        contentDescription = null
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(Spacing.Medium),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Today:",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                    Text(
                        "80%",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

            }
        }

        GirlFitInfoBubble(
            icon = Res.drawable.heart_icon_selected,
            text = "Click here to catch your favourite\nworkouts!",
            onClick = onFavouriteInfoBubbleClicked
        )
    }
}

@Composable
internal fun GoalHeaderAndPills(
    workoutCategory: WorkoutCategory,
    onWorkoutCategoryPillClicked: (WorkoutCategory) -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Text(
            "What's your goal?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(WorkoutCategory.entries.toTypedArray(), key = { it.name }) {
                WorkoutCategoryPill(
                    title = it.name.lowercase().replaceFirstChar { it.uppercase() },
                    isSelected = it == workoutCategory,
                    onClick = {
                        onWorkoutCategoryPillClicked(it)
                    }
                )
            }
        }
    }
}

@Composable
fun WorkoutCategoryPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            ).clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = Spacing.Medium, vertical = Spacing.ExtraSmall),
            text = title,
            style = if (isSelected) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodySmall
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WorkoutPreview() {
    WorkoutScreen(
        paddingValues = PaddingValues.Zero,
        uiState = WorkoutState(),
        event = {}
    )
}