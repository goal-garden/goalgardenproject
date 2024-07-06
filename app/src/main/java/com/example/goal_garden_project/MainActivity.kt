package com.example.goal_garden_project

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.goal_garden_project.navigation.Navigation
import com.example.goal_garden_project.reminder.NotificationHandler
import com.example.goal_garden_project.ui.theme.GoalgardenprojectTheme
import com.example.goal_garden_project.widgets.SimpleBottomBar

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
        val notificationHandler = NotificationHandler(this)

        notificationHandler.scheduleNotification()

            // Initialize WorkManager
            //WorkManager.initialize(this, Configuration.Builder().build())

    }
}