package com.creativehazio.meals.presentation.mealscandetails

import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.data.meal.domain.Meal
import com.creativehazio.data.meal.domain.MealNutrient
import com.creativehazio.data.meal.domain.MealScanResult
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.meals.util.Utils
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.back_icon
import girlfit.feature.meals.generated.resources.cal
import girlfit.feature.meals.generated.resources.protein
import girlfit.feature.meals.generated.resources.total_calories
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MealScanDetailScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    viewModel: MealScanDetailViewModel,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    MealScanDetailScreen(
        uiState = uiState,
        onEvent = event
    )
}

@Composable
internal fun MealScanDetailScreen(
    uiState: MealScanDetailState,
    onEvent: (MealScanDetailEvent) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier.fillMaxWidth()
                .height(400.dp)
        ) {
            //        AsyncImage(
//            modifier = Modifier.fillMaxWidth()
//                .height(400.dp),
//            model = uiState.meal.imageUrl,
//            contentDescription = null,
//            contentScale = ContentScale.Crop
//        )

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.protein),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
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

        Card(
            modifier = Modifier.offset(y = -Spacing.Large)
                .weight(1f),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding(start = Spacing.Medium, end = Spacing.Medium, top = Spacing.ExtraLarge),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
            ) {

                item(
                    span = { GridItemSpan(maxLineSpan) },
                ) {
                    GirlFitInfoBubble(
                        modifier = Modifier.fillMaxWidth(),
                        icon = Res.drawable.cal,
                        text = stringResource(Res.string.total_calories),
                        subText = "${uiState.meal.totalCalories} ",
                        centerItems = true
                    )
                }

                items(uiState.meal.mealNutrients, key = { it.id }) { mealNutrient ->
                    GirlFitInfoBubble(
                        icon = Utils.getIconForNutrient(mealNutrient.name),
                        text = mealNutrient.name,
                        subText = "${mealNutrient.gramTotal}g"
                    )
                }

                item(
                    span = { GridItemSpan(maxLineSpan) },
                ) {

                    val scanResultText = Utils.getTextForMealScanScore(uiState.meal.score)

                    Row(
                        modifier = Modifier.padding(top = Spacing.Large),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier.size(96.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "${uiState.meal.score.toInt()}%",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }

                        Text(
                            text = if (scanResultText == null) "" else stringResource(scanResultText),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            }
        }

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun MealScanDetailPreview() {
    MealScanDetailScreen(
        uiState = MealScanDetailState(
            meal = MealScanResult(
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
                ),
                score = 92f
            ),
        ),
        onEvent = {}
    )
}