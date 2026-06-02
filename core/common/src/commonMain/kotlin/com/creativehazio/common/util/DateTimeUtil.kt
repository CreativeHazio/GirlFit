package com.creativehazio.common.util

object DateTimeUtil {

    fun durationFormatter(durationInSeconds: Int): String {
        return if (durationInSeconds < 60) {
            "$durationInSeconds secs"
        } else {
            val minutes = durationInSeconds / 60
            "$minutes mins"
        }
    }

}