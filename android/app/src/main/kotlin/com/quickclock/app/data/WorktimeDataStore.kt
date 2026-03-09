package com.quickclock.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.quickclock.app.model.WorkSession
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore(name = "quickclock_data")

class WorktimeDataStore(private val context: Context) {
    private val SESSIONS_KEY = stringPreferencesKey("work_sessions")
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val sharedPrefs = context.getSharedPreferences("quickclock", Context.MODE_PRIVATE)
    
    val sessionsFlow: Flow<List<WorkSession>> = context.dataStore.data.map { preferences ->
        val sessionsJson = preferences[SESSIONS_KEY] ?: "[]"
        parseSessionsFromJson(sessionsJson)
    }
    
    suspend fun addSession(session: WorkSession) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[SESSIONS_KEY] ?: "[]"
            val sessions = parseSessionsFromJson(currentJson).toMutableList()
            sessions.add(session)
            val json = sessionsToJson(sessions)
            preferences[SESSIONS_KEY] = json
            // Also update SharedPreferences for synchronous access
            sharedPrefs.edit().putString("work_sessions", json).apply()
        }
    }
    
    suspend fun updateSession(session: WorkSession) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[SESSIONS_KEY] ?: "[]"
            val sessions = parseSessionsFromJson(currentJson).toMutableList()
            val index = sessions.indexOfFirst { it.id == session.id }
            if (index >= 0) {
                sessions[index] = session
                val json = sessionsToJson(sessions)
                preferences[SESSIONS_KEY] = json
                // Also update SharedPreferences for synchronous access
                sharedPrefs.edit().putString("work_sessions", json).apply()
            }
        }
    }
    
    private fun parseSessionsFromJson(json: String): List<WorkSession> {
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { index ->
                val obj = array.getJSONObject(index)
                WorkSession(
                    id = obj.getString("id"),
                    checkInTime = LocalDateTime.parse(obj.getString("checkInTime"), dateFormatter),
                    checkOutTime = if (obj.has("checkOutTime") && !obj.isNull("checkOutTime")) {
                        LocalDateTime.parse(obj.getString("checkOutTime"), dateFormatter)
                    } else null,
                    date = LocalDate.parse(obj.getString("date"))
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    private fun sessionsToJson(sessions: List<WorkSession>): String {
        val array = JSONArray()
        sessions.forEach { session ->
            val obj = JSONObject().apply {
                put("id", session.id)
                put("checkInTime", session.checkInTime.format(dateFormatter))
                put("checkOutTime", session.checkOutTime?.format(dateFormatter))
                put("date", session.date.toString())
            }
            array.put(obj)
        }
        return array.toString()
    }
}
