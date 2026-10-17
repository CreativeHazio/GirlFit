package com.creativehazio.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.creativehazio.common.util.DateTimeUtil
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutType
import com.creativehazio.designsystem.components.GirlFitWorkoutCard
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing

@Composable
internal fun WorkoutSection(
    name: String,
    workouts: List<Workout>,
    onNavigateToWorkoutDetail: (Workout) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Text(
            name,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.size(Spacing.ExtraSmall))
        workouts.forEach { workout ->
            GirlFitWorkoutCard(
                modifier = Modifier.fillMaxWidth().height(Sizing.CardHeightMedium),
                title = workout.title,
                titleStyle = MaterialTheme.typography.headlineSmall,
                durationText = workout.getDurationText(),
                detailsText = workout.getDetailsText(),
                imageUrl = workout.imageUrl,
                onCardClick = {
                    onNavigateToWorkoutDetail(workout)
                }
            )
        }
    }
}