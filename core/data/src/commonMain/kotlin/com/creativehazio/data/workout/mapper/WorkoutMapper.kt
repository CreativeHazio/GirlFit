package com.creativehazio.data.workout.mapper

import com.creativehazio.data.workout.data.local.ChallengeDayEntity
import com.creativehazio.data.workout.data.local.ExerciseEntity
import com.creativehazio.data.workout.data.local.WorkoutEntity
import com.creativehazio.data.workout.data.local.WorkoutWithExercises
import com.creativehazio.data.workout.data.remote.ChallengeDayDto
import com.creativehazio.data.workout.data.remote.ExerciseDto
import com.creativehazio.data.workout.data.remote.WorkoutDto
import com.creativehazio.data.workout.domain.Challenge
import com.creativehazio.data.workout.domain.ChallengeDay
import com.creativehazio.data.workout.domain.ChallengeDayState
import com.creativehazio.data.workout.domain.Exercise
import com.creativehazio.data.workout.domain.Workout
import com.creativehazio.data.workout.domain.WorkoutCategory
import com.creativehazio.data.workout.domain.WorkoutLevel
import com.creativehazio.data.workout.domain.WorkoutType

fun WorkoutDto.toWorkoutEntity() : WorkoutEntity {
    return WorkoutEntity(
        id = this.id,
        createdAt = this.createdAt,
        title = this.title,
        imageUrl = this.imageUrl,
        details = this.details,
        duration = this.duration,
        level = this.level,
        type = this.type,
        category = this.category
    )
}

fun ChallengeDayDto.toChallengeDayEntity(
    workoutId: String,
    state : String
) : ChallengeDayEntity {
    return ChallengeDayEntity(
        id = this.id,
        workoutId = workoutId,
        number = this.number,
        state = state
    )
}

fun ExerciseDto.toExerciseEntity(
    workoutId: String,
    isFavourite: Boolean
) : ExerciseEntity {
    return ExerciseEntity(
        id = this.id,
        workoutId = workoutId,
        title = this.title,
        duration = this.duration,
        description = this.description,
        thumbnailGifUrl = this.thumbnailGifUrl,
        gifUrl = this.gifUrl,
        isFavourite = isFavourite
    )
}

fun WorkoutWithExercises.toWorkout() : Workout {
    return Workout(
        id = workout.id,
        createdAt = workout.createdAt,
        title = workout.title,
        imageUrl = workout.imageUrl,
        details = workout.details,
        duration = workout.duration,
        level = WorkoutLevel.valueOf(workout.level),
        type = WorkoutType.valueOf(workout.type),
        category = WorkoutCategory.valueOf(workout.category),
        exercises = exercises.map { it.toExercise() },
        challenge = Challenge(
            // TODO: What should be the challenge ID
            id = "",
            challengeDays = challengeDays.map { it.toChallengeDay() }
        )
    )
}

fun ExerciseEntity.toExercise(): Exercise {
    return Exercise(
        id = id,
        title = title,
        duration = duration,
        description = description,
        thumbnailGifUrl = thumbnailGifUrl,
        gifUrl = gifUrl,
        isFavourite = isFavourite
    )
}


fun ChallengeDayEntity.toChallengeDay(): ChallengeDay {
    return ChallengeDay(
        id = id,
        number = number,
        state = ChallengeDayState.valueOf(state),
        workoutId = workoutId
    )
}