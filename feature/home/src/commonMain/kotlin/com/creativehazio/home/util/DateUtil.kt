package com.creativehazio.home.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

object DateUtil {

    fun getTodayIndex(): Int {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return today.dayOfWeek.isoDayNumber % 7
    }

    fun getTodayFormatted(): String {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val month = today.month.name.lowercase().replaceFirstChar { it.uppercase() }
        return "${today.day} $month"
    }

    fun getThisWeekDays(): List<String> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val daysSinceSunday = today.dayOfWeek.isoDayNumber % 7
        val sunday = today.minus(daysSinceSunday, DateTimeUnit.DAY)

        return (0..6).map { offset ->
            sunday.plus(offset, DateTimeUnit.DAY).day.toString().padStart(2, '0')
        }
    }

    val daysOfTheWeek = listOf(
        "Sun",
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat"
    )

}