package com.creativehazio.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.home.presentation.CycleCalender
import girlfit.feature.home.generated.resources.Res
import girlfit.feature.home.generated.resources.drop_emoji
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun HomeScreenCycleCalenderSection() {
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
                "14th day of Cycle",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
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