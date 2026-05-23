package com.creativehazio.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.creativehazio.designsystem.theme.Spacing

@Composable
fun GirlFitWorkoutCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
    detailsText: String? = null,
    durationText: String,
    buttonText: String? = null,
    onCardClick: () -> Unit
) {

    Card(
        modifier = modifier.clickable() {
            onCardClick()
        },
        shape = MaterialTheme.shapes.medium
    ) {
        Box(Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
            )

            Column(
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(Spacing.Medium)
            ) {
                Text(text = title, style = MaterialTheme.typography.titleLarge, color = Color.White)
                detailsText?.let {
                    Text(
                        text = detailsText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
                Row {
                    Text(
                        text = durationText,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }

            buttonText?.let {
                GirlFitPrimaryButton(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                        .padding(Spacing.Medium),
                    shape = MaterialTheme.shapes.medium,
                    text = buttonText,
                    onClick = {
                        onCardClick()
                    }
                )
            }

        }
    }
}
