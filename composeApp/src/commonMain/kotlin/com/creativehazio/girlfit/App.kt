package com.creativehazio.girlfit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import com.creativehazio.auth.presentation.signup.SignUpScreenRoot
import com.creativehazio.auth.presentation.signup.SignUpViewModel
import com.creativehazio.designsystem.components.InfoBubble
import com.creativehazio.designsystem.components.PrimaryButton
import com.creativehazio.designsystem.components.SearchBar
import com.creativehazio.designsystem.components.WorkoutCard
import com.creativehazio.designsystem.theme.GirlFitTheme
import org.jetbrains.compose.resources.painterResource

import girlfit.composeapp.generated.resources.Res
import girlfit.composeapp.generated.resources.carbohydrate
import okio.FileSystem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {

    SingletonImageLoader.setSafe { context ->
        ImageLoader.Builder(context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
                    .maxSizeBytes(50L * 1024 * 1024)
                    .build()
            }
            .build()
    }

    val signUpViewModel : SignUpViewModel = koinViewModel()

    GirlFitTheme {
        var showContent by remember { mutableStateOf(false) }
        var query by remember { mutableStateOf("")}
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .scrollable(rememberScrollState(), Orientation.Vertical),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            PrimaryButton(
//                text = "Click me!",
//                onClick = { showContent = !showContent }
//            )
//            Spacer(Modifier.size(20.dp))
//            SearchBar(
//                query = query,
//                onQueryChange = {
//                    query = it
//                },
//                placeholderText = "e.g weightloss meals",
//                onSearchPressed = {},
//                showFilterIcon = true,
//                onFilterClick = {}
//            )
//            Spacer(Modifier.size(20.dp))
//            WorkoutCard(
//                imageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400",
//                title = "Flat \nStomach",
//                durationText = "🕑7 mins",
//                buttonText = "Start",
//                onCardClick = {}
//            )
//            Spacer(Modifier.size(20.dp))
//            InfoBubble(
//                color = MaterialTheme.colorScheme.secondary,
//                icon = Res.drawable.carbohydrate,
//                text = "Carbohydrates",
//                subText = "100g"
//            )
//            Spacer(Modifier.size(20.dp))
            SignUpScreenRoot(
                signUpViewModel = signUpViewModel,
                onNavigateToHome = {},
                onNavigateToLogin = {}
            )
        }
    }
}