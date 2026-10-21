package com.creativehazio.designsystem.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.creativehazio.designsystem.shimmer

@Composable
fun GirlfitImage(
    imageUrl: String,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier
) {

    var isLoading by remember { mutableStateOf(true) }

    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.then(if (isLoading) Modifier.shimmer() else Modifier),
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading ||
                    state is AsyncImagePainter.State.Empty
        }
    )

}