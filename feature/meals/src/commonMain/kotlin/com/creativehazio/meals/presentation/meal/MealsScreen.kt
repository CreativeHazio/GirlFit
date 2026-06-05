package com.creativehazio.meals.presentation.meal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.components.GirlFitSearchBar
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.camera_track_calories
import girlfit.feature.meals.generated.resources.down_arrow
import girlfit.feature.meals.generated.resources.gradient_cam
import girlfit.feature.meals.generated.resources.meals_search_hint
import girlfit.feature.meals.generated.resources.meals_suggestions
import girlfit.feature.meals.generated.resources.pink_check_icon
import girlfit.feature.meals.generated.resources.premium_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MealsScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    viewModel: MealsViewModel,
    onNavigateToMealDetail: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when (it) {
                is MealsEffect.NavigateToMealsDetail -> {
                    onNavigateToMealDetail(it.mealId)
                }
            }
        }
    }

    MealsScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        onEvent = event
    )
}

@Composable
internal fun MealsScreen(
    uiState: MealsState,
    onEvent: (MealsEvent) -> Unit,
    paddingValues: PaddingValues
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
                HeaderSection()
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.size(54.dp))
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                SuggestionsHeadingAndSearch(
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
                        onEvent(MealsEvent.ApplyFilters)
                    }
                )
            }

            items(items = uiState.meals, key = { it.id }) {
                MealCard(
                    imageUrl = it.imageUrl,
                    onMealCardClicked = { onEvent(MealsEvent.OnMealCardClicked(it.id)) }
                )
            }
        }

        if (showMealFilterCard) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            showMealFilterCard = false
                            onEvent(MealsEvent.ApplyFilters)
                        }
                    )
            )
        }
    }
}

@Composable
internal fun HeaderSection() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = {}
        ) {
            Icon(
                modifier = Modifier.size(Sizing.IconMedium),
                painter = painterResource(Res.drawable.premium_icon),
                contentDescription = null
            )
        }

        GirlFitInfoBubble(
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 40.dp, end = Spacing.Medium),
            text = stringResource(Res.string.camera_track_calories),
        )

        Image(
            modifier = Modifier.align(Alignment.TopEnd)
                .padding(top = 87.dp, end = 180.dp)
                .size(60.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {

                },
            painter = painterResource(Res.drawable.gradient_cam),
            contentDescription = null
        )
    }
}

@Composable
internal fun SuggestionsHeadingAndSearch(
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
        Text(
            text = stringResource(Res.string.meals_suggestions),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.size(Spacing.Medium))

        GirlFitSearchBar(
            query = searchQuery,
            onQueryChange = { onSearchQueryChanged(it) },
            placeholderText = stringResource(Res.string.meals_search_hint),
            onSearchPressed = { onSearchPressed() },
            showFilterIcon = true,
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
                    MealsFilterCard(
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
internal fun MealCard(
    imageUrl: String,
    onMealCardClicked: () -> Unit
) {

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.height(Sizing.CardHeightLarge)
            .clickable {
                onMealCardClicked()
            }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }

}

@Composable
internal fun MealsFilterCard(
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun MealPreview() {
    MealsFilterCard(
        mealFilters = getDummyFilters(),
        onFilterItemClicked = {},
        filterItemIds = listOf(),
    )
}