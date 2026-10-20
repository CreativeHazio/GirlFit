package com.creativehazio.data.workout.data.remote

import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore

private const val WORKOUTS = "workouts"
private const val CREATED_AT = "createdAt"
private const val CYCLE_PHASE = "cyclePhase"


class WorkoutDataSource (
    private val firestore: FirebaseFirestore
) {

    suspend fun getWorkouts(
        limit: Int,
        lastTimestamp: Long? = null
    ): List<WorkoutDto> {
        var query = firestore.collection(WORKOUTS)
            .orderBy(CREATED_AT, Direction.DESCENDING)
            .limit(limit.toLong())

        if (lastTimestamp != null) {
            query = query.startAfterFieldValues { arrayOf<Any?>(lastTimestamp).forEach { add(it) } }
        }

        return query.get().documents.map { it.data() }
    }

    suspend fun getWorkoutsByPhase(
        limit: Int,
        cyclePhase: String
    ) : List<WorkoutDto> {
        val query = firestore.collection(WORKOUTS)
            .where { CYCLE_PHASE equalTo cyclePhase }
            .limit(limit.toLong())

        return query.get().documents.map { it.data() }
    }

}