package com.creativehazio.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import com.creativehazio.designsystem.theme.Spacing
import girlfit.core.designsystem.generated.resources.Res
import girlfit.core.designsystem.generated.resources.placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkoutCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
    detailsText: String? = null,
    durationText: String,
    buttonText: String,
    onCardClick: () -> Unit
) {

    Card(
        modifier = modifier.height(180.dp).width(170.dp),
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
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }

            PrimaryButton(
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
