package com.example.filroom.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeRange(val startTime: String, val endTime: String) {
    override fun toString(): String {
        return "$startTime - $endTime"
    }
}

fun toTimeRange(timeStr: String): TimeRange {
    val timeList = timeStr.split(Regex(" - "))
    return TimeRange(timeList[0], timeList[1])
}

fun checkDateRange(timeRange: TimeRange): Boolean {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH.mm")
    val formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val currentDate = LocalDateTime.now()
    val currentFormatted = currentDate.format(formatter2)

    val dateStart = LocalDateTime.parse("$currentFormatted ${timeRange.startTime}", formatter)
    val dateEnd = LocalDateTime.parse("$currentFormatted ${timeRange.endTime}", formatter)

    return currentDate.isAfter(dateStart) && currentDate.isBefore(dateEnd)
}