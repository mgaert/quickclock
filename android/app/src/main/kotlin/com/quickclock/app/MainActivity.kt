package com.quickclock.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quickclock.app.ui.theme.QuickClockTheme
import com.quickclock.app.viewmodel.WorktimeViewModel

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
        setContent {
            QuickClockTheme {
                QuickClockApp(viewModel)
            }
        }
    }
}

@Composable
fun QuickClockApp(viewModel: WorktimeViewModel) {
    val currentSession by viewModel.currentSession.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val todaySummary by viewModel.todaySummary.collectAsState()
    val message by viewModel.message.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            // Title
            Text(
                text = "QuickClock",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Current time display
            if (currentSession != null) {
                Text(
                    text = currentTime,
                    color = Color(0xFF00FF00),
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Running",
                    color = Color(0xFF00CC00),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = "00:00",
                    color = Color.Gray,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Idle",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Check-in/Check-out buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.checkIn() },
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                ) {
                    Text("IN", color = Color.White, fontSize = 11.sp)
                }
                
                Button(
                    onClick = { viewModel.checkOut() },
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    enabled = currentSession != null
                ) {
                    Text("OUT", color = Color.White, fontSize = 11.sp)
                }
            }
            
            // Today's summary
            Text(
                text = "Today: $todaySummary",
                color = Color(0xFFCCCCCC),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Message feedback
            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = Color(0xFF00FF00),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
