package com.creativehazio.girlfit

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import com.creativehazio.auth.data.FirebaseAuthService
import com.creativehazio.auth.presentation.emailverification.EmailVerificationScreenRoot
import com.creativehazio.auth.presentation.emailverification.EmailVerificationViewModel
import com.creativehazio.auth.presentation.login.LoginScreenRoot
import com.creativehazio.auth.presentation.login.LoginViewModel
import com.creativehazio.auth.presentation.signup.SignUpScreenRoot
import com.creativehazio.auth.presentation.signup.SignUpViewModel
import com.creativehazio.designsystem.components.BottomBarTab
import com.creativehazio.designsystem.components.GirlFitBottomBar
import com.creativehazio.designsystem.theme.GirlFitTheme
import com.creativehazio.girlfit.MainAppContainer
import com.creativehazio.girlfit.navConfig
import com.creativehazio.home.presentation.HomeScreenRoot
import com.creativehazio.home.presentation.HomeViewModel
import com.creativehazio.navigation.EmailVerification
import com.creativehazio.navigation.Home
import com.creativehazio.navigation.Login
import com.creativehazio.navigation.Main
import com.creativehazio.navigation.Me
import com.creativehazio.navigation.Meals
import com.creativehazio.navigation.Progress
import com.creativehazio.navigation.Route
import com.creativehazio.navigation.SignUp
import com.creativehazio.navigation.Workout
import com.creativehazio.navigation.WorkoutChallengeCalender
import com.creativehazio.navigation.WorkoutDetail
import com.creativehazio.workout.presentation.workout.WorkoutScreenRoot
import com.creativehazio.workout.presentation.workout.WorkoutViewModel
import com.creativehazio.workout.presentation.workoutchallengecalender.WorkoutChallengeCalenderScreenRoot
import com.creativehazio.workout.presentation.workoutchallengecalender.WorkoutChallengeCalenderViewModel
import com.creativehazio.workout.presentation.workoutdetail.WorkoutDetailScreenRoot
import com.creativehazio.workout.presentation.workoutdetail.WorkoutDetailState
import com.creativehazio.workout.presentation.workoutdetail.WorkoutDetailViewModel
import girlfit.composeapp.generated.resources.Res
import girlfit.composeapp.generated.resources.home
import girlfit.composeapp.generated.resources.home_selected
import girlfit.composeapp.generated.resources.me
import girlfit.composeapp.generated.resources.me_selected
import girlfit.composeapp.generated.resources.meals
import girlfit.composeapp.generated.resources.meals_selected
import girlfit.composeapp.generated.resources.progress
import girlfit.composeapp.generated.resources.progress_selected
import girlfit.composeapp.generated.resources.workout
import girlfit.composeapp.generated.resources.workout_selected
import io.ktor.http.parameters
import io.ktor.http.parametersOf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

import okio.FileSystem
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

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

    val backStack = rememberNavBackStack(navConfig, Main)

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
                    MainAppContainer(
                        onLogout = {
                            backStack.clear()
                            backStack.add(Login)
                        },
                        onNavigateToWorkoutChallengeCalender = { workoutId ->
                            backStack.add(WorkoutChallengeCalender(workoutId))
                        },
                        onNavigateToWorkoutDetail = { workoutId ->
                            backStack.add(WorkoutDetail(workoutId))
                        }
                    )
                }

                entry<WorkoutChallengeCalender> { key ->

                    val workoutChallengeCalenderViewModel : WorkoutChallengeCalenderViewModel = koinViewModel(
                        parameters = { parametersOf(key.id) }
                    )

                    WorkoutChallengeCalenderScreenRoot(
                        viewModel = workoutChallengeCalenderViewModel,
                        onBack = {
                            backStack.removeLastOrNull()
                        },
                        onNavigateToWorkoutDetail = { workoutId ->
                            backStack.add(WorkoutDetail(workoutId))
                        }
                    )
                }

                entry<WorkoutDetail> { key ->

                    val workoutDetailViewModel : WorkoutDetailViewModel = koinViewModel(
                        parameters = { parametersOf(key.id) }
                    )

                    WorkoutDetailScreenRoot(
                        workoutViewModel = workoutDetailViewModel,
                        onBack = {
                            backStack.removeLastOrNull()
                        }
                    )

                }

            }
        )

    }
}

@Composable
fun MainAppContainer(
    onLogout: () -> Unit,
    onNavigateToWorkoutDetail: (String) -> Unit,
    onNavigateToWorkoutChallengeCalender: (String) -> Unit
) {

    val tabBackStack = rememberNavBackStack(navConfig, Home)
    val currentTab = tabBackStack.lastOrNull() as? Route

    val tabs = listOf<BottomBarTab<Route>>(
        BottomBarTab(
            route = Home,
            title = "Home",
            unselectedIcon = Res.drawable.home,
            selectedIcon = Res.drawable.home_selected
        ),
        BottomBarTab(
            route = Workout,
            title = "Workout",
            unselectedIcon = Res.drawable.workout,
            selectedIcon = Res.drawable.workout_selected
        ),
        BottomBarTab(
            route = Progress,
            title = "Progress",
            unselectedIcon = Res.drawable.progress,
            selectedIcon = Res.drawable.progress_selected
        ),
        BottomBarTab(
            route = Meals,
            title = "Meals",
            unselectedIcon = Res.drawable.meals,
            selectedIcon = Res.drawable.meals_selected
        ),
        BottomBarTab(
            route = Me,
            title = "Me",
            unselectedIcon = Res.drawable.me,
            selectedIcon = Res.drawable.me_selected
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            GirlFitBottomBar(
                tabs = tabs,
                currentRoute = currentTab,
                onTabSelected = { selectedRoute ->
                    if (currentTab != selectedRoute) {
                        tabBackStack.clear()
                        tabBackStack.add(selectedRoute)
                    }
                }
            )
        }
    ) { innerPadding ->
        //TODO: Add onback to only close app when its homescreen
        NavDisplay(
            backStack = tabBackStack,
            entryProvider = entryProvider {
                entry<Home> {
                    val homeViewModel : HomeViewModel = koinViewModel()
                    HomeScreenRoot(
                        contentPaddingValues = innerPadding,
                        homeViewModel = homeViewModel,
                        onNavigateToWorkoutChallengeCalender = onNavigateToWorkoutChallengeCalender,
                        onNavigateToWorkoutDetail = onNavigateToWorkoutDetail
                    )
                }

                entry<Workout> {
                    val workoutViewModel : WorkoutViewModel = koinViewModel()

                    WorkoutScreenRoot(
                        paddingValues = innerPadding,
                        viewModel = workoutViewModel,
                        onNavigateToFavourite = {},
                        onNavigateToPersonalPlan = {},
                    )
                }

                entry<Progress> {

                }

                entry<Meals> {

                }

                entry<Me> {

                }
            }
        )
    }

}