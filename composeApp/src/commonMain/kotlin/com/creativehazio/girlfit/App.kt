package com.creativehazio.girlfit

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import com.creativehazio.auth.presentation.emailverification.EmailVerificationScreenRoot
import com.creativehazio.auth.presentation.emailverification.EmailVerificationViewModel
import com.creativehazio.auth.presentation.login.LoginViewModel
import com.creativehazio.auth.presentation.signup.SignUpScreenRoot
import com.creativehazio.auth.presentation.signup.SignUpViewModel
import com.creativehazio.designsystem.theme.GirlFitTheme

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
    val loginViewModel : LoginViewModel = koinViewModel()
    val emailVerificationViewModel : EmailVerificationViewModel = koinViewModel()

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
                onNavigateToEmailVerification = {},
                onNavigateToLogin = {}
            )

//            LoginScreenRoot(
//                loginViewModel = loginViewModel,
//                onNavigateToHome = {},
//                onNavigateToSignUp = {}
//            )

            EmailVerificationScreenRoot(
                email = "davideze123@gmail.com",
                emailVerificationViewModel = emailVerificationViewModel,
                onNavigateToLogin = {},
            )
        }
    }
}