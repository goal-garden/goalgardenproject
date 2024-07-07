package com.example.goal_garden_project.widgets

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimePickerButton(
    context: Context,
    notificationHour: Long,
    notificationMinute: Long,
    onTimeSelected: (Long, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    onTimeSelected(hourOfDay.toLong(), minute.toLong())
                },
                notificationHour.toInt(),
                notificationMinute.toInt(),
                true
            )
            timePickerDialog.show()
        },
        modifier = modifier
    ) {
        Text("Pick Notification Time")
    }
}
