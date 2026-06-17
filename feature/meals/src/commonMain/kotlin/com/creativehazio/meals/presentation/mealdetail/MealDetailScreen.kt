package com.creativehazio.meals.presentation.mealdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealNutrient
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.meals.util.Utils
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.back_icon
import girlfit.feature.meals.generated.resources.cal
import girlfit.feature.meals.generated.resources.carbohydrate
import girlfit.feature.meals.generated.resources.fat
import girlfit.feature.meals.generated.resources.fiber
import girlfit.feature.meals.generated.resources.protein
import girlfit.feature.meals.generated.resources.total_calories
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MealDetailScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    viewModel: MealDetailViewModel,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when(it) {
                MealDetailEffect.NavigateBack -> onBack()
            }
        }
    }

    MealDetailScreen(
        uiState = uiState,
        onEvent = event
    )
}

@Composable
internal fun MealDetailScreen(
    modifier: Modifier = Modifier,
    uiState: MealDetailState,
    onEvent: (MealDetailEvent) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            Modifier.fillMaxWidth()
                .height(400.dp)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth()
                    .height(400.dp),
                model = uiState.meal.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            IconButton(
                modifier = Modifier.padding(top = Spacing.Large),
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(Res.drawable.back_icon),
                    tint = Color.White,
                    contentDescription = null,
                )
            }
        }

        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = Spacing.Medium),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        ) {

            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                MealIngredientsList(
                    mealIngredients = uiState.meal.ingredients
                )
            }

            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                GirlFitInfoBubble(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background,
                    icon = Res.drawable.cal,
                    text = stringResource(Res.string.total_calories),
                    subText = "${ uiState.meal.totalCalories } ",
                    centerItems = true
                )
            }

            items(uiState.meal.mealNutrients, key = { it.id }) { mealNutrient ->
                GirlFitInfoBubble(
                    color = MaterialTheme.colorScheme.background,
                    icon = Utils.getIconForNutrient(mealNutrient.name),
                    text = mealNutrient.name,
                    subText = "${mealNutrient.gramTotal}g"
                )
            }
            item {
                Spacer(Modifier.navigationBarsPadding())
            }
        }


    }

}

@Composable
internal fun MealIngredientsList(
    mealIngredients: List<String>
) {
    Column(
        modifier = Modifier.padding(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        List(mealIngredients.size) {
            Text(
                text = " · ${ mealIngredients[it] } ",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun MealDetailPreview() {
    MealDetailScreen(
        uiState = MealDetailState(
            meal = Meal(
                id = "",
                name = "",
                imageUrl = "",
                ingredients = listOf(
                    "Grilled skinless chicken thighs",
                    "Rice",
                    "Green peas",
                    "Red ball peppers",
                    "Lemon slices",
                ),
                totalCalories = 1540,
                mealNutrients = listOf(
                    MealNutrient(
                        id = "1",
                        name = "Carbohydrate",
                        gramTotal = 100
                    ),
                    MealNutrient(
                        id = "2",
                        name = "Fat",
                        gramTotal = 25
                    ),
                    MealNutrient(
                        id = "3",
                        name = "Protein",
                        gramTotal = 100
                    ),
                    MealNutrient(
                        id = "4",
                        name = "Fiber",
                        gramTotal = 15
                    ),
                )
            )
        ),
        onEvent ={}
    )
}