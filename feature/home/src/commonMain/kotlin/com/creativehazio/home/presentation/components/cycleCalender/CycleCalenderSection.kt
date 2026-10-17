package com.creativehazio.home.presentation.components.cycleCalender

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.home.util.DateUtil
import girlfit.feature.home.generated.resources.Res
import girlfit.feature.home.generated.resources.drop_emoji
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun CycleCalenderSection(
    viewModel: CycleCalenderViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CycleCalenderSection(
        state = uiState,
    )

}

@Composable
private fun CycleCalenderSection(
    state: CycleCalenderState,
) {

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            Image(
                modifier = Modifier.size(Sizing.IconExtraLarge),
                painter = painterResource(Res.drawable.drop_emoji),
                contentDescription = null

            )
            Text(
                state.cycleDayText,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.size(Spacing.Medium))
        CycleCalender(
            todayLabel = state.todayLabel,
            weekDays = state.weekDays
        )
        Spacer(Modifier.size(Spacing.Medium))
        GirlFitInfoBubble(
            text = "Hormonal shift may lead to mood\n" +
                    "swings, bloating & fatigue"
        )
    }

}

@Composable
internal fun CycleCalender(todayLabel: String, weekDays: List<CalendarDay>) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Medium)
        ) {
            Text(
                todayLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.size(Spacing.Medium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDays.forEach { day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(day.label, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.size(Spacing.Small))
                        Text(
                            text = day.date,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.background(
                                color = if (day.isToday) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            ).padding(Spacing.ExtraSmall)
                        )
                    }
                }
            }

        }
    }
}