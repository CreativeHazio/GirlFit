package com.creativehazio.meals.presentation.meal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.designsystem.components.GirlfitEmptyState
import com.creativehazio.designsystem.components.GirlfitErrorLoadingItem
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
        uiState = uiState,
        onEvent = event
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun MealsScreen(
    uiState: MealsState,
    onEvent: (MealsEvent) -> Unit,
    paddingValues: PaddingValues,
    meals: LazyPagingItems<Meal>,
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

            stickyHeader(
                key = "meal_search"
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .statusBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                ) {
                    Text(
                        text = stringResource(Res.string.meals_suggestions),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    MealSearchBar(
                        searchQuery = uiState.searchQuery,
                        showFilterCard = showMealFilterCard,
                        onSearchQueryChanged = { onEvent(MealsEvent.OnSearchQueryChanged(it)) },
                        onSearchPressed = { onEvent(MealsEvent.OnSearchPressed) },
                        mealFilters = uiState.mealFilters,
                        filterItemIds = uiState.filterItemIds,
                        onFilterClicked = { showMealFilterCard = !showMealFilterCard },
                        onFilterItemClicked = {
                            onEvent(MealsEvent.OnFilterItemClicked(it))
                        },
                        onDismissFilter = {
                            showMealFilterCard = false
                        }
                    )
                }
            }

            when (val refreshState = meals.loadState.refresh) {
                is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }, contentType = "loading") {
                        Box(
                            Modifier.fillMaxWidth().padding(Spacing.Large),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularWavyProgressIndicator(modifier = Modifier.size(16.dp))
                        }
                    }
                }

                is LoadState.Error -> {
                    item(span = { GridItemSpan(maxLineSpan) }, contentType = "error") {
                        GirlfitErrorLoadingItem(
                            error = refreshState.error.message,
                            onRetryClicked = { meals.retry() },
                            modifier = Modifier
                        )
                    }
                }

                else -> {
                    val isEmpty = meals.itemCount == 0 &&
                            meals.loadState.refresh is LoadState.NotLoading &&
                            meals.loadState.append.endOfPaginationReached &&
                            meals.loadState.mediator?.refresh !is LoadState.Loading

                    if (isEmpty) {
                        item(span = { GridItemSpan(maxLineSpan) }, contentType = "empty_state") {
                            GirlfitEmptyState(
                                title = "Nothing to show here 💌",
                                subtitle = "We couldn't find any meals matching your criteria."
                            )
                        }
                    } else {
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

                        when (val append = meals.loadState.append) {
                            LoadState.Loading -> item(
                                span = { GridItemSpan(maxLineSpan) },
                                contentType = "append_loading"
                            ) {
                                Box(
                                    Modifier.fillMaxWidth().padding(Spacing.Medium),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularWavyProgressIndicator(modifier = Modifier.size(16.dp))
                                }
                            }

                            is LoadState.Error -> item(
                                span = { GridItemSpan(maxLineSpan) },
                                contentType = "append_error"
                            ) {
                                GirlfitErrorLoadingItem(
                                    error = append.error.message,
                                    onRetryClicked = { meals.retry() },
                                    modifier = Modifier
                                )
                            }

                            else -> {}
                        }
                    }
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