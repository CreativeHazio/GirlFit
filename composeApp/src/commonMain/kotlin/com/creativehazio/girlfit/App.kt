package com.creativehazio.girlfit

import androidx.compose.animation.SharedTransitionLayout
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.creativehazio.auth.presentation.emailverification.EmailVerificationScreenRoot
import com.creativehazio.auth.presentation.emailverification.EmailVerificationViewModel
import com.creativehazio.auth.presentation.login.LoginScreenRoot
import com.creativehazio.auth.presentation.login.LoginViewModel
import com.creativehazio.auth.presentation.signup.SignUpScreenRoot
import com.creativehazio.auth.presentation.signup.SignUpViewModel
import com.creativehazio.data.workout.domain.Exercise
import com.creativehazio.designsystem.LocalAnimatedVisibilityScope
import com.creativehazio.designsystem.LocalSharedTransitionScope
import com.creativehazio.designsystem.components.BottomBarTab
import com.creativehazio.designsystem.components.GirlFitBottomBar
import com.creativehazio.designsystem.renderAboveSharedElements
import com.creativehazio.designsystem.theme.GirlFitTheme
import com.creativehazio.home.presentation.HomeScreenRoot
import com.creativehazio.home.presentation.HomeViewModel
import com.creativehazio.me.presentation.MeScreenRoot
import com.creativehazio.me.presentation.MeViewModel
import com.creativehazio.meals.presentation.meal.MealsScreenRoot
import com.creativehazio.meals.presentation.meal.MealsViewModel
import com.creativehazio.meals.presentation.mealdetail.MealDetailScreenRoot
import com.creativehazio.meals.presentation.mealdetail.MealDetailViewModel
import com.creativehazio.meals.presentation.mealscan.MealScanScreenRoot
import com.creativehazio.meals.presentation.mealscan.MealScanViewModel
import com.creativehazio.meals.presentation.mealscandetails.MealScanDetailScreenRoot
import com.creativehazio.meals.presentation.mealscandetails.MealScanDetailViewModel
import com.creativehazio.navigation.EmailVerification
import com.creativehazio.navigation.StartWorkout
import com.creativehazio.navigation.Home
import com.creativehazio.navigation.Login
import com.creativehazio.navigation.Main
import com.creativehazio.navigation.Me
import com.creativehazio.navigation.MealDetail
import com.creativehazio.navigation.MealScan
import com.creativehazio.navigation.MealScanDetail
import com.creativehazio.navigation.Meals
import com.creativehazio.navigation.Onboarding
import com.creativehazio.navigation.Progress
import com.creativehazio.navigation.Route
import com.creativehazio.navigation.SignUp
import com.creativehazio.navigation.Splash
import com.creativehazio.navigation.Workout
import com.creativehazio.navigation.WorkoutChallengeCalender
import com.creativehazio.navigation.WorkoutDetail
import com.creativehazio.progress.presentation.ProgressScreenRoot
import com.creativehazio.progress.presentation.ProgressViewModel
import com.creativehazio.startup.onboarding.OnboardingScreenRoot
import com.creativehazio.startup.onboarding.OnboardingViewModel
import com.creativehazio.startup.splash.SplashScreenRoot
import com.creativehazio.startup.splash.SplashViewModel
import com.creativehazio.workout.presentation.exercise.ExerciseScreenRoot
import com.creativehazio.workout.presentation.exercise.ExerciseViewModel
import com.creativehazio.workout.presentation.workout.WorkoutScreenRoot
import com.creativehazio.workout.presentation.workout.WorkoutViewModel
import com.creativehazio.workout.presentation.workoutchallengecalender.WorkoutChallengeCalenderScreenRoot
import com.creativehazio.workout.presentation.workoutchallengecalender.WorkoutChallengeCalenderViewModel
import com.creativehazio.workout.presentation.workoutdetail.WorkoutDetailScreenRoot
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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
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

    val backStack = rememberNavBackStack(navConfig, Main)

    // TODO: Split all complex main screens into composable functions
    GirlFitTheme {

        val snappy = tween<IntOffset>(200, easing = FastOutSlowInEasing)
        val snappyFade = tween<Float>(200, easing = FastOutSlowInEasing)

        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this,
            ) {
                NavDisplay(
                    backStack = backStack,
                    onBack = {
                        if (backStack.size > 1) backStack.removeLastOrNull()
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    transitionSpec = {
                        (slideInHorizontally(snappy) { it / 3 } + fadeIn(snappyFade)) togetherWith
                                (slideOutHorizontally(snappy) { -it / 6 } + fadeOut(snappyFade))
                    },
                    popTransitionSpec = {
                        (slideInHorizontally(snappy) { -it / 6 } + fadeIn(snappyFade)) togetherWith
                                (slideOutHorizontally(snappy) { it / 3 } + fadeOut(snappyFade))
                    },
                    predictivePopTransitionSpec = {
                        (slideInHorizontally(snappy) { -it / 6 } + fadeIn(snappyFade)) togetherWith
                                (slideOutHorizontally(snappy) { it / 3 } + fadeOut(snappyFade))
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    entryProvider = entryProvider {
                        entry<Splash> {

                            val splashViewModel: SplashViewModel = koinViewModel()

                            SplashScreenRoot(
                                viewModel = splashViewModel,
                                onNavigateToOnboarding = {
                                    backStack.clear()
                                    backStack.add(Onboarding)
                                },
                                onNavigateToAuth = {
                                    backStack.clear()
                                    backStack.add(Login)
                                },
                                onNavigateToMain = {
                                    backStack.clear()
                                    backStack.add(Main)
                                },
                            )
                        }

                        entry<Onboarding> {
                            val onboardingViewModel: OnboardingViewModel = koinViewModel()

                            OnboardingScreenRoot(
                                viewModel = onboardingViewModel,
                                onNavigateToAuth = {
                                    backStack.clear()
                                    backStack.add(Login)
                                },
                                onNavigateToMain = {
                                    backStack.clear()
                                    backStack.add(Main)
                                }
                            )
                        }

                        entry<Login> {
                            val loginViewModel: LoginViewModel = koinViewModel()

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
                            val emailVerificationViewModel: EmailVerificationViewModel =
                                koinViewModel()

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
                            val signUpViewModel: SignUpViewModel = koinViewModel()

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
                            ProvideEntryAnimatedScope {
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
                                    },
                                    onNavigateToMealScan = {
                                        backStack.add(MealScan)
                                    },
                                    onNavigateToMealDetail = { mealId ->
                                        backStack.add(MealDetail(mealId))
                                    }
                                )
                            }
                        }

                        entry<WorkoutChallengeCalender> { key ->

                            val workoutChallengeCalenderViewModel: WorkoutChallengeCalenderViewModel =
                                koinViewModel(key = key.workoutId) {
                                    parametersOf(key.workoutId)
                                }

                            ProvideEntryAnimatedScope {
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

                        }

                        entry<WorkoutDetail> { key ->

                            val workoutDetailViewModel: WorkoutDetailViewModel =
                                koinViewModel(key = key.workoutId) {
                                    parametersOf(key.workoutId)
                                }

                            ProvideEntryAnimatedScope {

                                WorkoutDetailScreenRoot(
                                    workoutViewModel = workoutDetailViewModel,
                                    onNavigateToExercise = {
                                        backStack.add(StartWorkout(key.workoutId))
                                    },
                                    onBack = {
                                        backStack.removeLastOrNull()
                                    }
                                )
                            }


                        }

                        entry<StartWorkout> { key ->
                            val exerciseViewModel: ExerciseViewModel = koinViewModel(key = key.workoutId) {
                                parametersOf(key.workoutId)
                            }

                            ProvideEntryAnimatedScope {
                                ExerciseScreenRoot(
                                    viewModel = exerciseViewModel
                                )
                            }
                        }

                        entry<MealDetail> { key ->

                            val mealDetailViewModel: MealDetailViewModel =
                                koinViewModel(key = key.mealId) {
                                    parametersOf(key.mealId)
                                }

                            ProvideEntryAnimatedScope {
                                MealDetailScreenRoot(
                                    viewModel = mealDetailViewModel,
                                    onBack = {
                                        backStack.removeLastOrNull()
                                    }
                                )
                            }

                        }

                        entry<MealScan> {

                            val mealScanViewModel: MealScanViewModel = koinViewModel()

                            MealScanScreenRoot(
                                viewModel = mealScanViewModel
                            )

                        }

                        entry<MealScanDetail> {

                            val mealScanDetailViewModel: MealScanDetailViewModel = koinViewModel()

                            MealScanDetailScreenRoot(
                                viewModel = mealScanDetailViewModel
                            )

                        }

                    }
                )

            }
        }
    }
}

@Composable
fun MainAppContainer(
    onLogout: () -> Unit,
    onNavigateToWorkoutDetail: (String) -> Unit,
    onNavigateToWorkoutChallengeCalender: (String) -> Unit,
    onNavigateToMealScan: () -> Unit,
    onNavigateToMealDetail: (String) -> Unit
) {

    val tabBackStack = rememberNavBackStack(navConfig, Home)
    val currentTab = tabBackStack.lastOrNull() as? Route

    val tabs = rememberSaveable {
        listOf<BottomBarTab<Route>>(
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
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            GirlFitBottomBar(
                modifier = Modifier.renderAboveSharedElements(),
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
        val snappy = tween<IntOffset>(200, easing = FastOutSlowInEasing)
        val snappyFade = tween<Float>(200, easing = FastOutSlowInEasing)

        //TODO: Add onback to only close app when its homescreen
        NavDisplay(
            backStack = tabBackStack,
            transitionSpec = {
                (slideInHorizontally(snappy) { it / 3 } + fadeIn(snappyFade)) togetherWith
                        (slideOutHorizontally(snappy) { -it / 6 } + fadeOut(snappyFade))
            },

            popTransitionSpec = {
                (slideInHorizontally(snappy) { -it / 6 } + fadeIn(snappyFade)) togetherWith
                        (slideOutHorizontally(snappy) { it / 3 } + fadeOut(snappyFade))
            },
            entryProvider = entryProvider {
                entry<Home> {
                    val homeViewModel: HomeViewModel = koinViewModel()

                    HomeScreenRoot(
                        modifier = Modifier,
                        contentPaddingValues = innerPadding,
                        homeViewModel = homeViewModel,
                        onNavigateToWorkoutChallengeCalender = onNavigateToWorkoutChallengeCalender,
                        onNavigateToWorkoutDetail = onNavigateToWorkoutDetail
                    )
                }

                entry<Workout> {
                    val workoutViewModel: WorkoutViewModel = koinViewModel()

                    WorkoutScreenRoot(
                        paddingValues = innerPadding,
                        viewModel = workoutViewModel,
                        onNavigateToFavourite = {},
                        onNavigateToPersonalPlan = {},
                        onNavigateToWorkoutDetail = onNavigateToWorkoutDetail,
                        onNavigateToWorkoutChallengeCalender = onNavigateToWorkoutChallengeCalender
                    )
                }

                entry<Progress> {
                    val progressViewModel: ProgressViewModel = koinViewModel()

                    ProgressScreenRoot(
                        paddingValues = innerPadding,
                        viewModel = progressViewModel,
                        onNavigateToProgressCalender = {

                        }
                    )
                }

                entry<Meals> {
                    val mealsViewModel: MealsViewModel = koinViewModel()

                    MealsScreenRoot(
                        paddingValues = innerPadding,
                        viewModel = mealsViewModel,
                        onNavigateToMealScan = {
                            onNavigateToMealScan()
                        },
                        onNavigateToMealDetail = { mealId ->
                            onNavigateToMealDetail(mealId)
                        }
                    )
                }

                entry<Me> {

                    val meViewModel: MeViewModel = koinViewModel()

                    MeScreenRoot(
                        paddingValues = innerPadding,
                        viewModel = meViewModel
                    )
                }
            }
        )
    }

}

@Composable
fun ProvideEntryAnimatedScope(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAnimatedVisibilityScope provides LocalNavAnimatedContentScope.current,
        content = content
    )
}