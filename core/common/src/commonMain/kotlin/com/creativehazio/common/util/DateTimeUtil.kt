package com.creativehazio.common.util

object DateTimeUtil {

    private fun secondsToMinutes(seconds: Int) : Int {
        return if (seconds < 60) {
            seconds
        } else {
            seconds / 60
        }
    }

    fun durationFormatter(duration: Int) : String {
        val calculatedDuration = secondsToMinutes(duration)
        return if (calculatedDuration < 60) {
            "$calculatedDuration secs"
        } else {
            "$calculatedDuration mins"
        }
    }

}