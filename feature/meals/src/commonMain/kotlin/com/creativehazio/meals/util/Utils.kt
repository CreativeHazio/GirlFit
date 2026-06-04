package com.creativehazio.meals.util

import androidx.compose.runtime.Composable
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.carbohydrate
import girlfit.feature.meals.generated.resources.fat
import girlfit.feature.meals.generated.resources.fiber
import girlfit.feature.meals.generated.resources.protein
import girlfit.feature.meals.generated.resources.score_0_to_49
import girlfit.feature.meals.generated.resources.score_50_to_89
import girlfit.feature.meals.generated.resources.score_90_to_100
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

object Utils {
    @Composable
    internal fun getIconForNutrient(name: String) : DrawableResource? {
        return when (name) {
            stringResource(Res.string.carbohydrate) -> Res.drawable.carbohydrate
            stringResource(Res.string.fat) -> Res.drawable.fat
            stringResource(Res.string.protein) -> Res.drawable.protein
            stringResource(Res.string.fiber) -> Res.drawable.fiber
            else -> null
        }
    }

    @Composable
    fun getTextForMealScanScore(score: Float) : StringResource? {
        return when (score) {
            in 0f..49f -> {
                Res.string.score_0_to_49
            }
            in 50f..89f -> {
                Res.string.score_50_to_89
            }
            in 90f..100f -> {
                Res.string.score_90_to_100
            }
            else -> null
        }
    }
}