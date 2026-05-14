package com.creativehazio.girlfit

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import com.creativehazio.auth.presentation.emailverification.EmailVerificationScreenRoot
import com.creativehazio.auth.presentation.emailverification.EmailVerificationViewModel
import com.creativehazio.auth.presentation.login.LoginScreenRoot
import com.creativehazio.auth.presentation.login.LoginViewModel
import com.creativehazio.auth.presentation.signup.SignUpScreenRoot
import com.creativehazio.auth.presentation.signup.SignUpViewModel
import com.creativehazio.designsystem.theme.GirlFitTheme
import com.creativehazio.girlfit.navigation.EmailVerification
import com.creativehazio.girlfit.navigation.Login
import com.creativehazio.girlfit.navigation.Main
import com.creativehazio.girlfit.navigation.Route
import com.creativehazio.girlfit.navigation.SignUp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

import okio.FileSystem
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSerializationApi::class)
private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<Route>()
        }
    }
}

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

    val backStack = rememberNavBackStack(navConfig, Login)

    GirlFitTheme {

        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) backStack.removeLastOrNull()
            },
            entryProvider = entryProvider {
                entry<Login>{
                    val loginViewModel : LoginViewModel = koinViewModel()

                    LoginScreenRoot(
                        loginViewModel = loginViewModel,
                        onNavigateToSignUp = {
                            backStack.add(SignUp)
                        },
                        onNavigateToHome = {
                            backStack.clear()
                            backStack.add(Main)
                        },
                        onNavigateToEmailVerification = { email ->
                            backStack.add(EmailVerification(email))
                        }
                    )
                }

                entry<EmailVerification> { key ->
                    val emailVerificationViewModel : EmailVerificationViewModel = koinViewModel()

                    EmailVerificationScreenRoot(
                        email = key.email,
                        emailVerificationViewModel = emailVerificationViewModel,
                        onNavigateToLogin = {
                            backStack.clear()
                            backStack.add(Login)
                        }
                    )
                }

                entry<SignUp> {
                    val signUpViewModel : SignUpViewModel = koinViewModel()

                    SignUpScreenRoot(
                        signUpViewModel = signUpViewModel,
                        onNavigateToLogin = {
                            backStack.removeLastOrNull()
                        },
                        onNavigateToEmailVerification = { email ->
                            backStack.add(EmailVerification(email))
                        }
                    )
                }

                entry<Main> {

                }

            }
        )

    }
}