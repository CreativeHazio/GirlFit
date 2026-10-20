package com.creativehazio.workout.presentation.exercise

import androidx.lifecycle.viewModelScope
import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.data.workout.domain.Exercise
import com.creativehazio.data.workout.domain.WorkoutRepository
import girlfit.feature.workout.generated.resources.Res
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.time.TimeSource

data class Friend(
    val name: String,
    val progressPercent: Int,
    val isYou: Boolean = false,
)

data class ExerciseState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val exercises: List<Exercise> = emptyList(),
    val countdown: Int? = 3,
    val currentIndex: Int = 0,
    val durationMs: Long = 0L,
    val elapsedMs: Long = 0L,
    val isPaused: Boolean = false,
) : State {
    val currentExercise: Exercise? get() = exercises.getOrNull(currentIndex)

    /** Seconds left, rounded up so the UI shows "30 secs" at the very start. */
    val remainingSeconds: Int
        get() = ceil((durationMs - elapsedMs).coerceAtLeast(0L) / 1000.0).toInt()

    /** 0f..1f for the current exercise. */
    val exerciseProgress: Float
        get() = if (durationMs == 0L) 0f else (elapsedMs.toFloat() / durationMs).coerceIn(0f, 1f)

    /** 0f..1f for the whole workout. This drives the dashed bar at the top. */
    val overallProgress: Float
        get() = if (exercises.isEmpty()) 0f
        else ((currentIndex + exerciseProgress) / exercises.size).coerceIn(0f, 1f)

    val isCountingDown: Boolean get() = countdown != null

    val isLastExercise: Boolean get() = currentIndex == exercises.lastIndex
}

sealed interface ExerciseEvent : Event {
    data object TogglePause : ExerciseEvent
    data object NextExercise : ExerciseEvent
    data object IncreaseDuration : ExerciseEvent
    data object DecreaseDuration : ExerciseEvent
    data object BackClicked : ExerciseEvent
    data object InfoClicked : ExerciseEvent
    data object SpotifyClicked : ExerciseEvent
    data object RepeatClicked : ExerciseEvent
    data object AppBackgrounded : ExerciseEvent
}

sealed interface ExerciseEffect : Effect {
    data object NavigateBack : ExerciseEffect
    data class ShowExerciseInfo(val exercise: Exercise) : ExerciseEffect
    data object OpenSpotify : ExerciseEffect
    data object WorkoutComplete : ExerciseEffect
}

class ExerciseViewModel(
    workoutId: String,
    private val workoutRepository: WorkoutRepository,
) : BaseViewModel<ExerciseState, ExerciseEvent, ExerciseEffect>(
    ExerciseState()
) {

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {

            val lottieOne = Res.readBytes("files/pushup.json").decodeToString()
            val lottieTwo = Res.readBytes("files/lunges.json").decodeToString()
            val pool = listOf(lottieOne, lottieTwo)

            val workout = workoutRepository.getWorkout(workoutId)
            val exercises = workout.exercises.map {
                    exercise ->
                val randomAnimation = pool.random()
                exercise.copy(
                    thumbnailGifUrl = randomAnimation,
                    gifUrl = randomAnimation
                )
            }

            val firstExerciseDuration = exercises.firstOrNull()?.duration?.times(1000L) ?: 0L

            updateState {
                copy(
                    exercises = exercises,
                    durationMs = firstExerciseDuration
                )
            }
            startTimer()

        }
    }

    override fun onEvent(event: ExerciseEvent) {
        when (event) {
            ExerciseEvent.AppBackgrounded -> pause()
            ExerciseEvent.BackClicked -> {
                pause()
                sendEffect(ExerciseEffect.NavigateBack)
            }

            ExerciseEvent.DecreaseDuration -> changeDuration(-DURATION_STEP_MS)
            ExerciseEvent.IncreaseDuration -> changeDuration(+DURATION_STEP_MS)
            ExerciseEvent.InfoClicked -> {

            }

            ExerciseEvent.NextExercise -> advance()
            ExerciseEvent.RepeatClicked -> {
                updateState { copy(elapsedMs = 0L) }
            }

            ExerciseEvent.SpotifyClicked -> {}
            ExerciseEvent.TogglePause -> if (uiState.value.isPaused) resume() else pause()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            uiState.value.countdown?.let { runCountdown(from = it) }
            runTicker()
        }
    }

    private suspend fun runCountdown(from: Int) {
        for (n in from downTo 1) {
            updateState { copy(countdown = n) }
            delay(COUNTDOWN_STEP_MS)
        }
        updateState { copy(countdown = null) }
    }

    private suspend fun runTicker() = coroutineScope {
        var lastMark = TimeSource.Monotonic.markNow()
        while (isActive) {
            delay(TICK_MS)
            val deltaMs = lastMark.elapsedNow().inWholeMilliseconds
            lastMark = TimeSource.Monotonic.markNow()
            onTick(deltaMs)
        }
    }

    private fun onTick(deltaMs: Long) {
        val s = uiState.value

        if (s.isPaused || s.isCountingDown) return

        val newElapsed = s.elapsedMs + deltaMs
        if (newElapsed >= s.durationMs) {
            advance()
        } else {
            updateState { copy(elapsedMs = newElapsed) }
        }
    }

    private fun pause() {
        timerJob?.cancel()
        timerJob = null
        updateState { copy(isPaused = true) }
    }

    private fun resume() {
        updateState { copy(isPaused = false) }
        startTimer()
    }

    /** Moves to the next exercise, or finishes the workout after the last one. */
    private fun advance() {
        val s = uiState.value
        if (s.isLastExercise) {
            timerJob?.cancel()
            timerJob = null
            updateState { copy(elapsedMs = durationMs, isPaused = true) }
            sendEffect(ExerciseEffect.WorkoutComplete)
            return
        }
        val nextIndex = s.currentIndex + 1
        updateState {
            copy(
                currentIndex = nextIndex,
                durationMs = exercises[nextIndex].duration * 1000L,
                elapsedMs = 0L,
            )
        }
    }

    private fun changeDuration(deltaMs: Long) {
        val s = uiState.value
        val newDuration = (s.durationMs + deltaMs).coerceIn(MIN_DURATION_MS, MAX_DURATION_MS)
        // Never let the new duration land behind what has already been elapsed.
        val safeDuration = maxOf(newDuration, s.elapsedMs + MIN_REMAINING_MS)
        updateState { copy(durationMs = safeDuration) }
    }


    override fun onCleared() {
        timerJob?.cancel()
    }

    private companion object {
        const val COUNTDOWN_STEP_MS = 1_000L
        const val TICK_MS = 50L
        const val DURATION_STEP_MS = 5_000L
        const val MIN_DURATION_MS = 10_000L
        const val MAX_DURATION_MS = 300_000L
        const val MIN_REMAINING_MS = 1_000L
    }

}