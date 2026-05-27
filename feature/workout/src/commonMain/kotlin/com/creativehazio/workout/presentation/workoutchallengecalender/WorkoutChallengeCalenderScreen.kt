package com.creativehazio.workout.presentation.workoutchallengecalender

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.creativehazio.data.workout.domain.Challenge
import com.creativehazio.data.workout.domain.ChallengeDay
import com.creativehazio.data.workout.domain.ChallengeDayState
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.designsystem.theme.greyDisabledButtonLight
import com.creativehazio.designsystem.theme.textHighlightedLight
import girlfit.feature.workout.generated.resources.Res
import girlfit.feature.workout.generated.resources.add_friend_icon
import girlfit.feature.workout.generated.resources.back_icon
import girlfit.feature.workout.generated.resources.check_icon
import girlfit.feature.workout.generated.resources.trophy_icon
import girlfit.feature.workout.generated.resources.trophy_icon_completed
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkoutChallengeCalenderScreenRoot(
    viewModel: WorkoutChallengeCalenderViewModel,
    onBack: () -> Unit,
    onNavigateToWorkoutDetail: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when(it) {
                is WorkoutChallengeCalenderEffect.NavigateToWorkoutDetail -> {
                    onNavigateToWorkoutDetail(it.workoutId)
                }

                WorkoutChallengeCalenderEffect.NavigateBack -> onBack()
            }
        }
    }

    Scaffold { innerPadding ->
        WorkoutChallengeCalenderScreen(
            uiState = uiState,
            event = event,
        )
    }
}

@Composable
internal fun WorkoutChallengeCalenderScreen(
    modifier: Modifier = Modifier,
    uiState: WorkoutChallengeCalenderState,
    event: (WorkoutChallengeCalenderEvent) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(200.dp)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.Large),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        event(WorkoutChallengeCalenderEvent.OnBackClicked)
                    }) {
                        Icon(
                            painter = painterResource(Res.drawable.back_icon),
                            tint = Color.White,
                            contentDescription = null,
                        )
                    }

                    IconButton(onClick = { /* Handle action */ }) {
                        Icon(
                            painter = painterResource(Res.drawable.add_friend_icon),
                            tint = Color.White,
                            contentDescription = null,
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(start = Spacing.Medium),
                    text = uiState.workout.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.padding(start = Spacing.Medium),
                    text = "${uiState.workout.challenge.challengeTitle} Challenge",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(start = Spacing.Medium, end = Spacing.Medium)
                .navigationBarsPadding(),
        ) {
            val totalWeeks = uiState.workout.challenge.getChallengeWeeksFromDays()

            items(totalWeeks) { index ->
                val weekNumber = index + 1

                ChallengeWeekCard(
                    challenge = uiState.workout.challenge,
                    weekNumber = weekNumber,
                    isFirst = index == 0,
                    onNavigateToWorkoutDetail = {
                        event(WorkoutChallengeCalenderEvent.OnChallengeDayClicked(it))
                    }
                )
            }
        }
    }

}

@Composable
internal fun ChallengeWeekCard(
    challenge: Challenge,
    weekNumber: Int,
    isFirst: Boolean,
    onNavigateToWorkoutDetail: (String) -> Unit
) {

    val daysForThisWeek = remember(challenge.challengeDays, weekNumber) {
        val startDay = (weekNumber - 1) * Challenge.DAYS_IN_A_WEEK + 1
        val endDay = weekNumber * Challenge.DAYS_IN_A_WEEK

        challenge.challengeDays.filter { it.number in startDay..endDay }
    }

    val isWeekCompleted = challenge.isWeekCompleted(weekNumber)

    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {

        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .padding(
                        top = if (isFirst) 18.dp else 0.dp,
                    )
                    .background(textHighlightedLight)
            )

            Box(
                modifier = Modifier
                    .padding(top = 18.dp)
                    .size(Sizing.IconMedium)
                    .clip(CircleShape)
                    .background(if (isWeekCompleted) MaterialTheme.colorScheme.secondary else greyDisabledButtonLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.check_icon),
                    contentDescription = "Week Completed",
                    tint = if (isWeekCompleted) textHighlightedLight else Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Spacer(Modifier.size(Spacing.Small))
            Text(
                text = "Week $weekNumber",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth().height(Sizing.CardHeightMedium),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = MaterialTheme.shapes.medium
            ) {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FlowRow(
                        modifier = Modifier.fillMaxSize().padding(Spacing.Medium),
                        maxLines = 2,
                        maxItemsInEachRow = 4,
                        itemVerticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        daysForThisWeek.forEach {
                            ChallengeDayPill(
                                challengeDay = it,
                                onNavigateToWorkoutDetail = onNavigateToWorkoutDetail
                            )
                        }
                        Box(Modifier.size(32.dp))
                    }

                    Box(
                        modifier = Modifier.padding(end = 8.dp, bottom = 10.dp).align(Alignment.BottomEnd)
                    ) {
                        Image(
                            modifier = Modifier.size(Sizing.IconExtraLarge),
                            painter = painterResource(
                                if (isWeekCompleted) Res.drawable.trophy_icon_completed else Res.drawable.trophy_icon
                            ),
                            contentDescription = null
                        )
                    }
                }
            }
        }

    }
}

@Composable
internal fun ChallengeDayPill(
    challengeDay: ChallengeDay,
    onNavigateToWorkoutDetail: (String) -> Unit
) {

    when (challengeDay.state) {
        ChallengeDayState.COMPLETED -> {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(Sizing.IconSmall),
                    painter = painterResource(Res.drawable.check_icon),
                    contentDescription = "Completed",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        ChallengeDayState.CURRENT -> {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .clickable {
                        onNavigateToWorkoutDetail(challengeDay.workout.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color(0xFF67D7A0),
                        style = Stroke(
                            width = 3f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                        )
                    )
                }
                Text(
                    text = challengeDay.number.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        ChallengeDayState.UPCOMING -> {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant, shape = CircleShape)
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = challengeDay.number.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChallengeDayPillPrev() {

    val challenge = Challenge(
        challengeDays = listOf(
            ChallengeDay(
                number = 1,
                state = ChallengeDayState.COMPLETED,
            ),
            ChallengeDay(
                number = 2,
                state = ChallengeDayState.COMPLETED,
            ),
            ChallengeDay(
                number = 3,
                state = ChallengeDayState.COMPLETED,
            ),
            ChallengeDay(
                number = 4,
                state = ChallengeDayState.COMPLETED,
            ),
            ChallengeDay(
                number = 5,
                state = ChallengeDayState.COMPLETED,
            ),
            ChallengeDay(
                number = 6,
                state = ChallengeDayState.COMPLETED,
            ),
            ChallengeDay(
                number = 7,
                state = ChallengeDayState.CURRENT,
            ),
            ChallengeDay(
                number = 8,
                state = ChallengeDayState.UPCOMING,
            ),
            ChallengeDay(
                number = 9,
                state = ChallengeDayState.UPCOMING,
            ),
            ChallengeDay(
                number = 10,
                state = ChallengeDayState.UPCOMING,
            ),
            ChallengeDay(
                number = 11,
                state = ChallengeDayState.UPCOMING,
            ),
            ChallengeDay(
                number = 12,
                state = ChallengeDayState.UPCOMING,
            ),
            ChallengeDay(
                number = 13,
                state = ChallengeDayState.UPCOMING,
            ),
            ChallengeDay(
                number = 14,
                state = ChallengeDayState.UPCOMING,
            ),
        )
    )

    WorkoutChallengeCalenderScreen(
        uiState = WorkoutChallengeCalenderState(
            workout = Workout(
                challenge = challenge
            )
        ),
        event = {}
    )
}

