package com.quickclock.app.model

import java.time.LocalDateTime
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class WorkSession(
    val id: String = System.currentTimeMillis().toString(),
    val checkInTime: LocalDateTime,
    val checkOutTime: LocalDateTime? = null,
    val date: LocalDate = checkInTime.toLocalDate()
) {
    fun durationMinutes(): Long {
        val endTime = checkOutTime ?: LocalDateTime.now()
        return ChronoUnit.MINUTES.between(checkInTime, endTime)
    }
    
    fun durationString(): String {
        val minutes = durationMinutes()
        val hours = minutes / 60
        val mins = minutes % 60
        return String.format("%02d:%02d", hours, mins)
    }
    
    fun checkInTimeString(): String = checkInTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
    
    fun checkOutTimeString(): String? = 
        checkOutTime?.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
    
    fun isActive(): Boolean = checkOutTime == null
}
