package com.creativehazio.girlfit

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.creativehazio.designsystem.components.BottomBarTab
import com.creativehazio.designsystem.components.GirlFitBottomBar
import com.creativehazio.designsystem.renderAboveSharedElements
import com.creativehazio.home.presentation.HomeScreenRoot
import com.creativehazio.home.presentation.HomeViewModel
import com.creativehazio.me.presentation.MeScreenRoot
import com.creativehazio.me.presentation.MeViewModel
import com.creativehazio.meals.presentation.meal.MealsScreenRoot
import com.creativehazio.meals.presentation.meal.MealsViewModel
import com.creativehazio.navigation.Home
import com.creativehazio.navigation.Me
import com.creativehazio.navigation.Meals
import com.creativehazio.navigation.Progress
import com.creativehazio.navigation.Route
import com.creativehazio.navigation.Workout
import com.creativehazio.navigation.navConfig
import com.creativehazio.progress.presentation.ProgressScreenRoot
import com.creativehazio.progress.presentation.ProgressViewModel
import com.creativehazio.workout.presentation.workout.WorkoutScreenRoot
import com.creativehazio.workout.presentation.workout.WorkoutViewModel
import girlfit.composeapp.generated.resources.Res
import girlfit.composeapp.generated.resources.home
import girlfit.composeapp.generated.resources.home_selected
import girlfit.composeapp.generated.resources.me
import girlfit.composeapp.generated.resources.me_selected
import girlfit.composeapp.generated.resources.meals
import girlfit.composeapp.generated.resources.meals_selected
import girlfit.composeapp.generated.resources.progress
import girlfit.composeapp.generated.resources.progress_selected
import girlfit.composeapp.generated.resources.tab_home
import girlfit.composeapp.generated.resources.tab_me
import girlfit.composeapp.generated.resources.tab_meals
import girlfit.composeapp.generated.resources.tab_progress
import girlfit.composeapp.generated.resources.tab_workout
import girlfit.composeapp.generated.resources.workout
import girlfit.composeapp.generated.resources.workout_selected
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.ExperimentalCompottieApi
import io.github.alexzhirkevich.compottie.Lottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Resource
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel

private val mainTabs = listOf(
    BottomBarTab(Home, Res.string.tab_home, Res.drawable.home, Res.drawable.home_selected),
    BottomBarTab(Workout, Res.string.tab_workout, Res.drawable.workout, Res.drawable.workout_selected),
    BottomBarTab(Progress, Res.string.tab_progress, Res.drawable.progress, Res.drawable.progress_selected),
    BottomBarTab(Meals, Res.string.tab_meals, Res.drawable.meals, Res.drawable.meals_selected),
    BottomBarTab(Me, Res.string.tab_me, Res.drawable.me, Res.drawable.me_selected),
)

@OptIn(ExperimentalResourceApi::class, ExperimentalCompottieApi::class)
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            GirlFitBottomBar(
                modifier = Modifier.renderAboveSharedElements(),
                tabs = mainTabs,
                currentRoute = currentTab,
                onTabSelected = { selectedRoute ->
                    if (currentTab != selectedRoute) {
                        tabBackStack.clear()
                        tabBackStack.add(selectedRoute)
                    }
                }
            )
        },
        floatingActionButton = {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.Resource(Res.getUri("files/FitnessBuddyAI.json"))
            )

            Lottie(
                modifier = Modifier.size(77.dp),
                painter = rememberLottiePainter(
                    composition = composition,
                    iterations = Compottie.IterateForever
                ),
                contentDescription = "Lottie animation"
            )
        },
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