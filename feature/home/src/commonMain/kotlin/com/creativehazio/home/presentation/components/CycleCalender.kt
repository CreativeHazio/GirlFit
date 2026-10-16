package com.creativehazio.home.presentation.components

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.creativehazio.designsystem.theme.Spacing

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
            Text(
                "2 December",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.size(Spacing.Medium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfTheWeek.forEachIndexed { index, day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(day, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.size(Spacing.Small))
                        Text(
                            text = thisWeekDay[index],
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.background(
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