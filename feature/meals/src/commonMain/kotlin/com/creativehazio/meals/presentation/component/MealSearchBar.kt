package com.creativehazio.meals.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.creativehazio.data.meal.domain.MealFilter
import com.creativehazio.designsystem.components.GirlFitSearchBar
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.down_arrow
import girlfit.feature.meals.generated.resources.meals_search_hint
import girlfit.feature.meals.generated.resources.pink_check_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MealSearchBar(
    searchQuery: String,
    showFilterCard: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSearchPressed: () -> Unit,
    mealFilters: List<MealFilter>,
    filterItemIds: List<String>,
    onFilterClicked: () -> Unit,
    onFilterItemClicked: (String) -> Unit,
    onDismissFilter: () -> Unit
) {
    Column {

        GirlFitSearchBar(
            query = searchQuery,
            onQueryChange = { onSearchQueryChanged(it) },
            placeholderText = stringResource(Res.string.meals_search_hint),
            onSearchPressed = { onSearchPressed() },
            showFilterIcon = true,
            filterIconBadgeCount = filterItemIds.size,
            onFilterClick = onFilterClicked
        )

        if (showFilterCard) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Popup(
                    alignment = Alignment.TopEnd,
                    onDismissRequest = { onDismissFilter() },
                    properties = PopupProperties(focusable = true)
                ) {
                    MealFilterCard(
                        mealFilters = mealFilters,
                        filterItemIds = filterItemIds,
                        onFilterItemClicked = {
                            onFilterItemClicked(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun MealFilterCard(
    mealFilters: List<MealFilter>,
    filterItemIds: List<String>,
    onFilterItemClicked: (String) -> Unit,
) {

    var expandedFilterIndex by remember { mutableIntStateOf(0) }

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {

        Column(
            modifier = Modifier.padding(Spacing.Large),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            mealFilters.forEachIndexed { index, mealFilter ->
                val isExpanded = expandedFilterIndex == index

                val arrowRotation by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    label = "arrow_rotation"
                )

                Row(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        expandedFilterIndex = if (isExpanded) expandedFilterIndex else index
                    },
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = mealFilter.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Icon(
                        modifier = Modifier.graphicsLayer {
                            rotationZ = arrowRotation
                        },
                        painter = painterResource(Res.drawable.down_arrow),
                        contentDescription = null
                    )
                }

                AnimatedVisibility(visible = isExpanded) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
                    ) {
                        mealFilter.items.forEach { filterItem ->
                            Row(
                                modifier = Modifier.width(200.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                    ) {
                                        onFilterItemClicked(filterItem.id)
                                    },
                                horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = filterItem.name,
                                    style = MaterialTheme.typography.labelLarge
                                )
                                if (filterItemIds.contains(filterItem.id)) {
                                    Icon(
                                        painter = painterResource(Res.drawable.pink_check_icon),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }

    }

}