package com.creativehazio.progress.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.skydoves.landscapist.image.LandscapistImage
import girlfit.core.designsystem.generated.resources.filter_ic
import girlfit.core.designsystem.generated.resources.time_icon
import girlfit.feature.progress.generated.resources.Res
import girlfit.feature.progress.generated.resources.barbell_squat_icon
import girlfit.feature.progress.generated.resources.down_arrow
import girlfit.feature.progress.generated.resources.filter_ic
import girlfit.feature.progress.generated.resources.fire_icon
import girlfit.feature.progress.generated.resources.pen_icon
import girlfit.feature.progress.generated.resources.time_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProgressScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    viewModel: ProgressViewModel,
    onNavigateToProgressCalender: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    ProgressScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        onEvent = event
    )
}

@Composable
internal fun ProgressScreen(
    paddingValues: PaddingValues,
    uiState: ProgressState,
    onEvent: (ProgressEvent) -> Unit
) {

    LazyColumn(
        modifier = Modifier.padding(horizontal = Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large),
        contentPadding = paddingValues
    ) {

        item {
            ProgressHeader()
        }

        item {
            HistorySection()
        }

        item {
            WeightSection(
                unit = "kg"
            )
        }

        item {
            BMISection(
                height = 171.0,
                heightUnit = "cm"
            )
        }

    }

}

@Composable
internal fun ProgressHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        ProgressHeaderItem(
            icon = Res.drawable.barbell_squat_icon,
            title = "Workouts",
            value = 721
        )
        ProgressHeaderItem(
            icon = Res.drawable.fire_icon,
            title = "Calories",
            value = 10809
        )
        ProgressHeaderItem(
            icon = Res.drawable.time_icon,
            title = "Minutes",
            value = 676
        )

    }
}

@Composable
internal fun ProgressHeaderItem(
    icon: DrawableResource,
    title: String,
    value: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
        ) {
            Icon(
                modifier = Modifier.size(Sizing.IconMedium),
                painter = painterResource(icon),
                tint = Color.Unspecified,
                contentDescription = null
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
internal fun HistorySection() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        shape = MaterialTheme.shapes.medium
    ) {

        Column(
            modifier = Modifier.padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "History",
                    style = MaterialTheme.typography.labelLarge
                )
                TextButton(onClick = {}) {
                    Text(
                        text = "View all",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            WorkoutHistoryCard(
                imageUrl = "",
                title = "Full body workout",
                duration = 12,
                calories = 160,
                formattedTime = "8:00, Jan 4 2026"
            )

            WorkoutHistoryCard(
                imageUrl = "",
                title = "Full body stretch",
                duration = 12,
                calories = 160,
                formattedTime = "8:00, Dec 14 2026"
            )
        }

    }
}

@Composable
internal fun WorkoutHistoryCard(
    imageUrl: String,
    title: String,
    duration: Int,
    calories: Int,
    formattedTime: String,
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        LandscapistImage(
            modifier = Modifier.size(60.dp),
            imageModel = { imageUrl }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.labelMedium
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
            ) {
                Icon(
                    modifier = Modifier.size(Sizing.IconSmall),
                    painter = painterResource(Res.drawable.time_icon),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
                Text(
                    text = "$duration mins",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
            ) {
                Icon(
                    modifier = Modifier.size(Sizing.IconSmall),
                    painter = painterResource(Res.drawable.fire_icon),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
                Text(
                    text = "$calories" + "Kcal",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

}

@Composable
internal fun WeightSection(
    unit: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weight",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "($unit)",
                style = MaterialTheme.typography.labelMedium
            )
            Icon(
                painter = painterResource(Res.drawable.down_arrow),
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            WeightProgressItem(
                title = "Current",
                value = "120.0",
                canChangeValue = true
            )
            WeightProgressItem(
                title = "Goal",
                value = "70.0",
                canChangeValue = true
            )
            WeightProgressItem(
                title = "Date",
                hasFilter = true,
                value = "Jan 2025",
            )
        }

        WeightProgressGraph()

    }
}

@Composable
internal fun WeightProgressItem(
    title: String,
    hasFilter: Boolean = false,
    value: String,
    canChangeValue: Boolean = false
) {

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            if (hasFilter) {
                Spacer(Modifier.size(Spacing.ExtraSmall))
                IconButton(
                    modifier = Modifier.size(Sizing.IconSmall),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        painter = painterResource(Res.drawable.filter_ic),
                        contentDescription = null
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            if (canChangeValue) {
                Spacer(Modifier.size(Spacing.ExtraSmall))
                IconButton(
                    modifier = Modifier.size(Sizing.IconMedium),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        painter = painterResource(Res.drawable.pen_icon),
                        contentDescription = null
                    )
                }
            }
        }
    }

}

@Composable
internal fun WeightProgressGraph() {
    val yearList = rememberSaveable {
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep","Oct", "Nov", "Dec")
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            yearList.forEach {
                Text(text = it, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
internal fun BMISection(
    height: Double,
    heightUnit: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        shape = MaterialTheme.shapes.medium
    ) {

        Column(
            modifier = Modifier.padding(Spacing.Large),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "BMI")
                Text(text = "KG/m2")
                Text(text = "Based on your height")
            }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Height")
                    Text(text = "($heightUnit)")
                    Spacer(Modifier.size(Spacing.Small))
                    Icon(
                        painter = painterResource(Res.drawable.down_arrow),
                        contentDescription = null
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${height}cm",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.size(Spacing.ExtraSmall))
                    IconButton(
                        modifier = Modifier.size(Sizing.IconMedium),
                        onClick = {}
                    ) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            painter = painterResource(Res.drawable.pen_icon),
                            contentDescription = null
                        )
                    }
                }
            }

            Column {
                Box {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Green)
                        )
                        Spacer(Modifier.size(Spacing.ExtraSmall))
                        Text(text = "You")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                }
            }

        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
internal fun ProgressScreenPreview() {
    ProgressScreen(
        paddingValues = PaddingValues.Zero,
        uiState = ProgressState(),
        onEvent = {}
    )
}
