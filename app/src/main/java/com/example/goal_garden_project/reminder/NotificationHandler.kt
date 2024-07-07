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
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goal_garden_project.R
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.viewmodels.AddViewModel
import com.example.goal_garden_project.viewmodels.AddViewModelFactory
import com.example.goal_garden_project.viewmodels.DetailViewModel
import com.example.goal_garden_project.viewmodels.DetailViewModelFactory
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Calendar

class NotificationHandler(private val context: Context) {
    private val channelId = "goal_notifications"
    private val channelName = "Goal Notifications"
    private val channelDescription = "Notifications for goal achievements"
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    val db = AppDatabase.getDatabase(context)

    val goalRepository = GoalRepository(goalDao = db.goalDao())
    fun getGoalImageById(id: Long): Flow<String> = flow {
        goalRepository.getCurrentPlantImage(id).collect { url ->
            emit(url)
        }
    }

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

        coroutineScope.launch {
            // Collect the URL from getGoalImageById
            val url = getGoalImageById(goalId).first() // Use first() to get the first emitted value

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(
                    context.resources.getIdentifier(
                        url, "drawable", context.packageName
                    )
                )
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

                }
                notify(goalId.toInt(), notification)

            }

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
            putExtra("goalId", goalId)
        //putExtra("goalImage", goalImage)      //would be better
        }
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
    @RequiresApi(Build.VERSION_CODES.M)
    fun cancelNotification(goalId: Long) {
        with(NotificationManagerCompat.from(context)) {
            cancel(goalId.toInt())
        }
        cancelScheduledNotification(goalId)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun cancelScheduledNotification(goalId: Long) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            goalId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun requestExactAlarmPermission() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        context.startActivity(intent)
    }


}
