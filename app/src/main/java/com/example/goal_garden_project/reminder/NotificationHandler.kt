package com.example.goal_garden_project.reminder

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.goal_garden_project.R
import kotlinx.coroutines.launch
import java.util.Calendar

class NotificationHandler(private val context: Context) {
    private val channelId = "goal_notifications"
    private val channelName = "Goal Notifications"
    private val channelDescription = "Notifications for goal achievements"


    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification( goalId: Long, title: String, message: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.erd1)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //request permission
                return
            }
            notify(goalId.toInt(), notification)

        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun scheduleNotification(goalId:Long, interval:Long, time:Long) { //in milliseconds
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            println("versions")
            if (!alarmManager.canScheduleExactAlarms()) {
                println("request")
                requestExactAlarmPermission()
                return
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("interval", interval)
            putExtra("goalId", goalId)}
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            goalId.toInt(), //this must be unique so it doesnt get overwritten the next time
            intent,

            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            println("lower then version s")

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        } else {
            println("version s or higher")
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                interval,
                pendingIntent
            )
        }

    }

    private fun requestExactAlarmPermission() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        context.startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleDailyNotification() {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        if (!alarmManager.canScheduleExactAlarms()) {
            requestExactAlarmPermission()
            return
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 2)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,      //this must be unique so it doesnt get overwritten the next time
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }


}
