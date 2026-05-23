package com.creativehazio.workout.presentation.workoutchallengecalender

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.designsystem.theme.greyDisabledButtonLight
import com.creativehazio.designsystem.theme.textHighlightedLight
import girlfit.feature.workout.generated.resources.Res
import girlfit.feature.workout.generated.resources.add_friend_icon
import girlfit.feature.workout.generated.resources.back_icon
import girlfit.feature.workout.generated.resources.check_icon
import girlfit.feature.workout.generated.resources.trophy_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkoutChallengeCalenderScreenRoot(
    viewModel: WorkoutChallengeCalenderViewModel,
    onBack: () -> Unit,
    onNavigateToWorkoutDetail: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    Scaffold { innerPadding ->
        WorkoutChallengeCalenderScreen(
            uiState = uiState,
            onEvent = event,
            weeks = MockChallengeData
        )
    }
}

@Composable
internal fun WorkoutChallengeCalenderScreen(
    modifier: Modifier = Modifier,
    weeks: List<ChallengeWeek>,
    uiState: WorkoutChallengeCalenderState,
    onEvent: (WorkoutChallengeCalenderEvent) -> Unit
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

                    IconButton(onClick = {}) {
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
                    text = "De-Stress",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.padding(start = Spacing.Medium),
                    text = "7x4 Challenge",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            itemsIndexed(weeks) { index, week ->
                TimelineNode(
                    week = week,
                    isFirst = index == 0,
                    isLast = index == weeks.lastIndex
                )
            }
        }
    }

}

enum class DayState { COMPLETED, CURRENT, UPCOMING, TROPHY }

data class ChallengeDay(
    val dayNumber: Int,
    val state: DayState
)

data class ChallengeWeek(
    val weekNumber: Int,
    val isCompleted: Boolean, // Determines if the left timeline circle is pink or grey
    val days: List<ChallengeDay>
)

@Composable
fun TimelineNode(
    week: ChallengeWeek,
    isFirst: Boolean,
    isLast: Boolean
) {
    // IntrinsicSize.Min is the magic that makes the line stretch to fit the card!
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {

        // 1. The Timeline Graphic Column
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            // The vertical connecting line
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    // Don't draw the line above the first item or below the last item
                    .padding(
                        top = if (isFirst) 24.dp else 0.dp,
                        bottom = if (isLast) 24.dp else 0.dp
                    )
                    .background(textHighlightedLight)
            )

            // The Checkmark Circle
            val circleColor =
                if (week.isCompleted) MaterialTheme.colorScheme.secondary else greyDisabledButtonLight
            val iconTint = if (week.isCompleted) textHighlightedLight else Color.White

            Box(
                modifier = Modifier
                    .padding(top = 20.dp) // Aligns circle with the "Week X" text
                    .size(Sizing.IconMedium)
                    .clip(CircleShape)
                    .background(circleColor),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    painter = painterResource(Res.drawable.check_icon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(Sizing.IconSmall)
                )

            }
        }

        // 2. The Content Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, bottom = 32.dp) // Spacing below the card
        ) {
            Text(
                text = "Week ${week.weekNumber}",
            )
            Spacer(Modifier.size(Spacing.Medium))
            WeekCard(days = week.days)
        }
    }
}

@Composable
fun WeekCard(days: List<ChallengeDay>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(vertical = 20.dp, horizontal = 24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // First Row (Days 1-4)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.take(4).forEach { DayItem(it) }
            }
            // Second Row (Days 5-7 + Trophy)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.drop(4).take(4).forEach { DayItem(it) }
            }
        }
    }
}

@Composable
fun DayItem(day: ChallengeDay) {
    Box(
        modifier = Modifier.size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        when (day.state) {
            DayState.COMPLETED -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.check_icon),
                        contentDescription = "Completed",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(Sizing.IconSmall)
                    )
                }
            }

            DayState.CURRENT -> {
                // The dashed border effect
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
                    text = day.dayNumber.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
            }

            DayState.UPCOMING -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            1.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.dayNumber.toString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            DayState.TROPHY -> {
                // Replace this Text with your actual Trophy Drawable!
                Image(
                    modifier = Modifier.size(Sizing.IconExtraLarge),
                    painter = painterResource(Res.drawable.trophy_icon),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun TNodePreview() {

    WorkoutChallengeCalenderScreen(
        weeks = MockChallengeData,
        uiState = WorkoutChallengeCalenderState(),
        onEvent = {}
    )
}

val MockChallengeData = listOf(
    ChallengeWeek(
        weekNumber = 1,
        isCompleted = true, // Turns the left circle pink and adds the checkmark
        days = listOf(
            ChallengeDay(1, DayState.COMPLETED),
            ChallengeDay(2, DayState.COMPLETED),
            ChallengeDay(3, DayState.COMPLETED),
            ChallengeDay(4, DayState.COMPLETED),
            ChallengeDay(5, DayState.COMPLETED),
            ChallengeDay(6, DayState.COMPLETED),
            ChallengeDay(7, DayState.COMPLETED),
            ChallengeDay(-1, DayState.TROPHY) // -1 acts as a placeholder for the trophy slot
        )
    ),
    ChallengeWeek(
        weekNumber = 2,
        isCompleted = true,
        days = listOf(
            ChallengeDay(8, DayState.COMPLETED),
            ChallengeDay(9, DayState.COMPLETED),
            ChallengeDay(10, DayState.COMPLETED),
            ChallengeDay(11, DayState.COMPLETED),
            ChallengeDay(12, DayState.COMPLETED),
            ChallengeDay(13, DayState.COMPLETED),
            ChallengeDay(14, DayState.COMPLETED),
            ChallengeDay(-1, DayState.TROPHY)
        )
    ),
    ChallengeWeek(
        weekNumber = 3,
        isCompleted = false, // Keeps the left circle grey
        days = listOf(
            ChallengeDay(15, DayState.COMPLETED),
            ChallengeDay(16, DayState.COMPLETED),
            ChallengeDay(17, DayState.COMPLETED),
            ChallengeDay(18, DayState.COMPLETED),
            ChallengeDay(19, DayState.COMPLETED),
            ChallengeDay(20, DayState.CURRENT), // This will draw the dashed circle!
            ChallengeDay(21, DayState.UPCOMING),
            ChallengeDay(-1, DayState.TROPHY)
        )
    ),
    ChallengeWeek(
        weekNumber = 4,
        isCompleted = false,
        days = listOf(
            ChallengeDay(22, DayState.UPCOMING),
            ChallengeDay(23, DayState.UPCOMING),
            ChallengeDay(24, DayState.UPCOMING),
            ChallengeDay(25, DayState.UPCOMING),
            ChallengeDay(26, DayState.UPCOMING),
            ChallengeDay(27, DayState.UPCOMING),
            ChallengeDay(28, DayState.UPCOMING),
            ChallengeDay(-1, DayState.TROPHY)
        )
    )
)