package com.creativehazio.meals.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.creativehazio.designsystem.components.GirlfitImage
import com.creativehazio.designsystem.theme.Sizing

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

        GirlfitImage(
            imageUrl = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

}