package com.example.goal_garden_project.reminder


/*
    private fun setReminder(goal: Goal) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderBroadcastReceiver::class.java).apply {
            putExtra("goalId", goal.goalId)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, goal.goalId.toInt(), intent, 0)

        val intervalMillis = when (goal.reminderInterval) {
            0 -> AlarmManager.INTERVAL_DAY * 7
            else -> AlarmManager.INTERVAL_DAY * goal.reminderInterval
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, (goal.notificationTime / (60 * 60 * 1000)).toInt())
            set(Calendar.MINUTE, (goal.notificationTime / (60 * 1000) % 60).toInt())
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            intervalMillis,
            pendingIntent
        )
    }
}

 */
