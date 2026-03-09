package com.quickclock.app

import android.app.KeyguardManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quickclock.app.ui.theme.QuickClockTheme
import com.quickclock.app.viewmodel.WorktimeViewModel
import com.quickclock.app.model.WorkSession

class MainActivity : ComponentActivity() {
    private val viewModel: WorktimeViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WorktimeViewModel(applicationContext) as T
                }
            }
        ).get(WorktimeViewModel::class.java)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            QuickClockTheme {
                QuickClockApp(viewModel) { 
                    // Callback: Go home after check-in
                    goHome()
                }
            }
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "worktime_channel",
                "Work Time Tracking",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for work time tracking"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showWorkingNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, "worktime_channel")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("QuickClock")
            .setContentText("I am in - Working...")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
    
    fun hideWorkingNotification() {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
    }
    
    private fun goHome() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
    }
}

@Composable
fun QuickClockApp(viewModel: WorktimeViewModel, onCheckInComplete: () -> Unit = {}) {
    val currentSession by viewModel.currentSession.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val todaySummary by viewModel.todaySummary.collectAsState()
    val message by viewModel.message.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val mainActivity = context as? MainActivity
    
    // Show/hide notification based on session state
    LaunchedEffect(currentSession) {
        if (currentSession != null) {
            mainActivity?.showWorkingNotification()
        } else {
            mainActivity?.hideWorkingNotification()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Title
            item {
                Text(
                    text = "QuickClock",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Current session info
            item {
                if (currentSession != null) {
                    val session = currentSession!!
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = currentTime,
                            color = Color(0xFF00FF00),
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "I am in",
                            color = Color(0xFF00CC00),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "IN: ${session.checkInTimeString()}",
                            color = Color(0xFF00FF00),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "00:00",
                            color = Color.Gray,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "I am out",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            // Check-in/Check-out buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { 
                            viewModel.checkIn()
                            mainActivity?.postDelayed({ onCheckInComplete() }, 500)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                    ) {
                        Text("IN", color = Color.White, fontSize = 10.sp)
                    }
                    
                    Button(
                        onClick = { viewModel.checkOut() },
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp),
                        enabled = currentSession != null
                    ) {
                        Text("OUT", color = Color.White, fontSize = 10.sp)
                    }
                }
            }
            
            // Today's summary
            item {
                Text(
                    text = "Today: $todaySummary",
                    color = Color(0xFFCCCCCC),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Session log header
            if (sessions.isNotEmpty()) {
                item {
                    Text(
                        text = "Sessions:",
                        color = Color(0xFFFFFFFF),
                        fontSize = 9.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Sessions list
            val todaySessions = sessions.filter { 
                it.date == java.time.LocalDate.now() 
            }.reversed()
            
            items(todaySessions) { session ->
                SessionItem(session)
            }
            
            // Message feedback
            if (message.isNotEmpty()) {
                item {
                    Text(
                        text = message,
                        color = Color(0xFF00FF00),
                        fontSize = 9.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun SessionItem(session: WorkSession) {
    val inTime = session.checkInTimeString()
    val outTime = session.checkOutTimeString() ?: "--:--"
    val duration = session.durationString()
    
    Text(
        text = "$inTime → $outTime ($duration)",
        color = Color(0xFFAAAAFF),
        fontSize = 8.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

// Extension für delayed execution
private fun MainActivity.postDelayed(action: () -> Unit, delayMillis: Long) {
    this.window.decorView.postDelayed(action, delayMillis)
}
