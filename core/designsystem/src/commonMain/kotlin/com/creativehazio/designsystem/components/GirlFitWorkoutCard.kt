package com.creativehazio.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.core.designsystem.generated.resources.Res
import girlfit.core.designsystem.generated.resources.time_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun GirlFitWorkoutCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    detailsText: String? = null,
    durationText: String? = null,
    buttonText: String? = null,
    onCardClick: () -> Unit
) {

    Card(
        modifier = modifier.clickable {
            onCardClick()
        },
        shape = MaterialTheme.shapes.medium
    ) {
        Box(Modifier.fillMaxSize()) {
            AsyncImage(
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
                    .padding(Spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Text(text = title, style = titleStyle, color = Color.White)
                detailsText?.let {
                    Text(
                        text = detailsText,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
                durationText?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                    ) {
                        Image(
                            modifier = Modifier.size(Sizing.IconSmall),
                            painter = painterResource(Res.drawable.time_icon),
                            contentDescription = null
                        )
                        Text(
                            text = durationText,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
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
