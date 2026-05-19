package com.creativehazio.home.domain

data class RecommendedWorkout(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val detailsText: String? = null,
    val durationText: String = "",
)
