package com.quickclock.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quickclock.app.data.WorktimeDataStore
import com.quickclock.app.model.WorkSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalDate

class WorktimeViewModel(context: Context) : ViewModel() {
    private val dataStore = WorktimeDataStore(context)
    
    private val _sessions = MutableStateFlow<List<WorkSession>>(emptyList())
    val sessions: StateFlow<List<WorkSession>> = _sessions.asStateFlow()
    
    private val _currentSession = MutableStateFlow<WorkSession?>(null)
    val currentSession: StateFlow<WorkSession?> = _currentSession.asStateFlow()
    
    private val _currentTime = MutableStateFlow("00:00")
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()
    
    private val _todaySummary = MutableStateFlow("00:00")
    val todaySummary: StateFlow<String> = _todaySummary.asStateFlow()
    
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()
    
    init {
        viewModelScope.launch {
            dataStore.sessionsFlow.collect { sessions ->
                _sessions.value = sessions
                updateCurrentSession()
                updateTodaySummary()
            }
        }
        
        // Update time display every second
        viewModelScope.launch {
            while (true) {
                updateCurrentSession()
                kotlinx.coroutines.delay(1000)
            }
        }
    }
    
    fun checkIn() {
        viewModelScope.launch {
            val newSession = WorkSession(
                checkInTime = LocalDateTime.now(),
                date = LocalDate.now()
            )
            dataStore.addSession(newSession)
            _message.value = "Checked in"
        }
    }
    
    fun checkOut() {
        viewModelScope.launch {
            val current = _currentSession.value
            if (current != null) {
                val updatedSession = current.copy(checkOutTime = LocalDateTime.now())
                dataStore.updateSession(updatedSession)
                _message.value = "Checked out"
            } else {
                _message.value = "No active session"
            }
        }
    }
    
    fun exportCSV(): String {
        return dataStore.exportToCSV(_sessions.value)
    }
    
    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            dataStore.deleteSession(sessionId)
            _message.value = "Session deleted"
        }
    }
    
    fun clearAllSessions() {
        viewModelScope.launch {
            dataStore.clearAllSessions()
            _message.value = "All sessions cleared"
        }
    }
    
    private fun updateCurrentSession() {
        val today = LocalDate.now()
        val activeSessions = _sessions.value.filter { 
            it.date == today && it.isActive() 
        }
        _currentSession.value = activeSessions.lastOrNull()
        
        if (_currentSession.value != null) {
            _currentTime.value = _currentSession.value!!.durationString()
        }
    }
    
    private fun updateTodaySummary() {
        val today = LocalDate.now()
        val todaySessions = _sessions.value.filter { it.date == today }
        val totalMinutes = todaySessions.sumOf { it.durationMinutes() }
        val hours = totalMinutes / 60
        val mins = totalMinutes % 60
        _todaySummary.value = String.format("%02d:%02d", hours, mins)
    }
}
