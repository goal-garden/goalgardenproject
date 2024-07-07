package com.example.goal_garden_project.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable

class NotificationReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationReceiver", "Broadcast received")
        val interval = intent.getLongExtra("interval", 0L)
        val time = intent.getLongExtra("time", 0L)
        val goalId = intent.getLongExtra("goalId", 0L)
        //val image = intent.getStringExtra("goalImage") for later maybe


        // Handle the notification logic here
        //Toast.makeText(context, "Time to work on your goal!", Toast.LENGTH_SHORT).show()
        val notificationHandler = NotificationHandler(context)
        notificationHandler.sendNotification(goalId,"Goal Reminder"+goalId.toString(), "Don't forget to work on your goals!")
        Log.d("NotificationReceiver", "Notification sent")


        // Reschedule the alarm only for older versions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S && interval > 0) {
            println("reschedule manually")
            val notificationHandler = NotificationHandler(context)
            val nextTime = time + interval
            notificationHandler.scheduleNotification(goalId, interval, nextTime)
        }
    }
}
