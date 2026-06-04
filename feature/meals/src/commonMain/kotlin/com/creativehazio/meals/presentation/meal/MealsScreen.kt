package com.creativehazio.meals.presentation.meal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.components.GirlFitSearchBar
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.camera_track_calories
import girlfit.feature.meals.generated.resources.gradient_cam
import girlfit.feature.meals.generated.resources.meals_search_hint
import girlfit.feature.meals.generated.resources.meals_suggestions
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
            when(it){
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

    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = Spacing.Medium),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        contentPadding = paddingValues
    ) {

        item(
            span = { GridItemSpan(maxLineSpan) },
        ) {
            HeaderSection()
        }

        item(
            span = { GridItemSpan(maxLineSpan) },
        ) {
            Spacer(Modifier.size(54.dp))
        }

        item(
            span = { GridItemSpan(maxLineSpan) },
        ) {
            SuggestionsHeadingAndSearch(
                searchQuery = uiState.searchQuery,
                onSearchQueryChanged = {

                },
                onSearchPressed = {

                }
            )
        }

        items(items = uiState.meals, key = { it.id }) {
            MealCard(
                imageUrl = it.imageUrl,
                onMealCardClicked = {
                    onEvent(MealsEvent.OnMealCardClicked(it.id))
                }
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
            Box(modifier = Modifier.size(24.dp).background(Color.Green, CircleShape))
        }

        GirlFitInfoBubble(
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 40.dp, end = Spacing.Medium),
            text = stringResource(Res.string.camera_track_calories),
        )

        Image(
            modifier = Modifier.align(Alignment.TopEnd)
                .padding(top = 87.dp, end = 180.dp)
                .size(60.dp),
            painter = painterResource(Res.drawable.gradient_cam),
            contentDescription = null
        )
    }
}

@Composable
internal fun SuggestionsHeadingAndSearch(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchPressed: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        Text(
            text = stringResource(Res.string.meals_suggestions),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        GirlFitSearchBar(
            query = searchQuery,
            onQueryChange = {
                onSearchQueryChanged(it)
            },
            placeholderText = stringResource(Res.string.meals_search_hint),
            onSearchPressed = {
                onSearchPressed()
            },
            showFilterIcon = true,
            onFilterClick = {

            }
        )
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
//        AsyncImage(
//            modifier = Modifier.fillMaxSize(),
//            model = imageUrl,
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//        )

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(Res.drawable.gradient_cam),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun MealPreview() {
    MealsScreen(
        uiState = MealsState(),
        onEvent = {},
        paddingValues = PaddingValues.Zero
    )
}