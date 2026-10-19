package com.creativehazio.meals.presentation.meal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.meals.presentation.component.MealCard
import com.creativehazio.meals.presentation.component.MealHeaderSection
import com.creativehazio.meals.presentation.component.MealSearchBar
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.meals_suggestions
import org.jetbrains.compose.resources.stringResource

@Composable
fun MealsScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    viewModel: MealsViewModel,
    onNavigateToMealScan: () -> Unit,
    onNavigateToMealDetail: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    val meals = viewModel.meals.collectAsLazyPagingItems()
    val mealFilterIds = viewModel.filterItemIds.collectAsStateWithLifecycle().value

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when (it) {
                is MealsEffect.NavigateToMealDetail -> {
                    onNavigateToMealDetail(it.mealId)
                }

                MealsEffect.NavigateToMealScan -> {
                    onNavigateToMealScan()
                }
            }
        }
    }

    MealsScreen(
        paddingValues = paddingValues,
        meals = meals,
        mealFilterIds = mealFilterIds,
        uiState = uiState,
        onEvent = event
    )
}

@Composable
internal fun MealsScreen(
    uiState: MealsState,
    onEvent: (MealsEvent) -> Unit,
    paddingValues: PaddingValues,
    meals: LazyPagingItems<Meal>,
    mealFilterIds: List<String>
) {

    var showMealFilterCard by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = Spacing.Medium),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
            contentPadding = paddingValues
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                MealHeaderSection(
                    onMealScanCamClicked = {
                        onEvent(MealsEvent.OnMealScanCamClicked)
                    }
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.size(54.dp))
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(Res.string.meals_suggestions),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.size(Spacing.Medium))

                MealSearchBar(
                    searchQuery = uiState.searchQuery,
                    showFilterCard = showMealFilterCard,
                    onSearchQueryChanged = { onEvent(MealsEvent.OnSearchQueryChanged(it)) },
                    onSearchPressed = { onEvent(MealsEvent.OnSearchPressed) },
                    mealFilters = uiState.mealFilters,
                    filterItemIds = mealFilterIds,
                    onFilterClicked = { showMealFilterCard = !showMealFilterCard },
                    onFilterItemClicked = {
                        onEvent(MealsEvent.OnFilterItemClicked(it))
                    },
                    onDismissFilter = {
                        showMealFilterCard = false
                    }
                )
            }

            items(
                count = meals.itemCount,
                key = meals.itemKey { it.id },
            ) { index ->
                val meal = meals[index]

                if (meal != null) {
                    MealCard(
                        sharedElementKeyId = meal.id,
                        imageUrl = meal.imageUrl,
                        onMealCardClicked = { onEvent(MealsEvent.OnMealCardClicked(meal.id)) }
                    )
                }
            }
        }

        if (showMealFilterCard) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun MealPreview() {

}