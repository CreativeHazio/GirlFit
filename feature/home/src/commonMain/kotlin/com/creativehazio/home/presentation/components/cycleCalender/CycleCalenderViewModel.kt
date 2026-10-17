package com.creativehazio.home.presentation.components.cycleCalender

import com.creativehazio.common.BaseViewModel
import com.creativehazio.common.Effect
import com.creativehazio.common.Event
import com.creativehazio.common.State
import com.creativehazio.home.util.DateUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CycleCalenderState(
    val cycleDayText: String = "",
    val todayLabel: String = "",
    val weekDays: List<CalendarDay> = emptyList()
) : State

data class CalendarDay(
    val label: String,
    val date: String,
    val isToday: Boolean
)

sealed interface CycleCalenderEvent : Event {
    data object Refresh : CycleCalenderEvent
}

sealed interface CycleCalenderEffect : Effect {}

class CycleCalenderViewModel(
) : BaseViewModel<CycleCalenderState, CycleCalenderEvent, CycleCalenderEffect>(
    CycleCalenderState()
) {

    init {
        onEvent(CycleCalenderEvent.Refresh)
    }

    override fun onEvent(event: CycleCalenderEvent) {
        when(event){
            CycleCalenderEvent.Refresh -> refresh()
        }
    }

    fun refresh() {
        val dates = DateUtil.getThisWeekDays()
        val todayIndex = DateUtil.getTodayIndex()

        // TODO: replace with real cycle data from your repository
        val cycleDay = 14

        updateState { copy(
            cycleDayText = "${cycleDay.toOrdinal()} day of Cycle",
            todayLabel = DateUtil.getTodayFormatted(),
            weekDays = DateUtil.daysOfTheWeek.mapIndexed { index, label ->
                CalendarDay(
                    label = label,
                    date = dates[index],
                    isToday = index == todayIndex
                )
            }
        ) }
    }
}

private fun Int.toOrdinal(): String {
    val suffix = when {
        this % 100 in 11..13 -> "th"
        this % 10 == 1 -> "st"
        this % 10 == 2 -> "nd"
        this % 10 == 3 -> "rd"
        else -> "th"
    }
    return "$this$suffix"
}