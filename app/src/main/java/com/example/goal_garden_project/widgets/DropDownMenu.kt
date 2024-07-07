package com.example.goal_garden_project.widgets

import android.app.AlarmManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.models.GoalWithPlantPicture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDropdownMenu(
    context: Context,
    goalsWithPlantPicture: List<GoalDao.IdImageTitle>?, //image, title , id (1 mal plant, 1 mal goal)
    plantName: String,
    onPlantSelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = plantName,
            onValueChange = { /* No-op */ },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable { expanded = true },
            readOnly = true,
            label = { Text("Plant") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            goalsWithPlantPicture?.forEach { plant ->
                DropdownMenuItem(
                    leadingIcon = {
                        Image(
                            painter = painterResource(
                                id = context.resources.getIdentifier(
                                    plant.image, "drawable", context.packageName
                                )
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(90.dp)
                        )
                    },
                    text = { Text(text = plant.title) },
                    onClick = {
                        onPlantSelected(plant.id.toString(), plant.title)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderIntervalDropdown(
    initValue: Long,
    onIntervalSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val intervalOptions = listOf(
        "Every Minute",
        "Daily",
        "Every 2 days",
        "Every 3 days",
        "Every 4 days",
        "Every 5 days",
        "Every 6 days",
        "Every week"
    )

    val intervalValues = mapOf(
        "Every Minute" to 60 * 1000L,
        "Daily" to AlarmManager.INTERVAL_DAY,
        "Every 2 days" to 2 * AlarmManager.INTERVAL_DAY,
        "Every 3 days" to 3 * AlarmManager.INTERVAL_DAY,
        "Every 4 days" to 4 * AlarmManager.INTERVAL_DAY,
        "Every 5 days" to 5 * AlarmManager.INTERVAL_DAY,
        "Every 6 days" to 6 * AlarmManager.INTERVAL_DAY,
        "Every week" to AlarmManager.INTERVAL_DAY * 7
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = intervalValues.entries.find { it.value == initValue }?.key?:"", //selectedInterval,
            onValueChange = { /* No-op */ },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clickable { expanded = true },
            readOnly = true,
            label = { Text("Interval") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            intervalOptions.forEach { interval ->
                DropdownMenuItem(
                    text = { Text(text = interval) },
                    onClick = {
                        expanded = false
                        onIntervalSelected(intervalValues[interval] ?: AlarmManager.INTERVAL_DAY)
                    }
                )
            }
        }
    }
}