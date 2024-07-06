package com.example.goal_garden_project.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("NotificationReceiver", "Broadcast received")
        if (context != null) {
            val notificationHandler = NotificationHandler(context)
            notificationHandler.sendNotification("Goal Reminder", "Don't forget to work on your goals!")
            Log.d("NotificationReceiver", "Notification sent")
        }
    }
}
