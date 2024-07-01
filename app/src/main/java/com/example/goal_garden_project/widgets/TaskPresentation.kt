package com.example.goal_garden_project.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.goal_garden_project.models.Task


    @Composable
    fun TaskList(tasks: List<Task>, selectedTasks: List<Long> = emptyList(), onTaskSelectionChange: (Long, Boolean) -> Unit = { _, _ -> } ){


        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    selectedTasks = selectedTasks,
                    onTaskSelectionChange = onTaskSelectionChange
                )
            }
        }

    }

    @Composable
    fun TaskItem(task: Task, selectedTasks: List<Long>, onTaskSelectionChange: (Long, Boolean) -> Unit ){

        var isSelected by remember { mutableStateOf(false) }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    isSelected = !isSelected
                    onTaskSelectionChange(task.taskId, isSelected)
                           }, // Handle click on the whole Card
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor =  if (isSelected) Color.Gray else Color.LightGray,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = task.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Date: ${task.date}", style = MaterialTheme.typography.bodyMedium)
                }
                if (task.isFulfilled) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Fulfilled",
                        tint = Color.DarkGray
                    )
                }
                /*
                Icon(
                    imageVector = if (task.isFulfilled) Icons.Default.Check else Icons.Default.Build,
                    contentDescription = if (task.isFulfilled) "Fulfilled" else "Unfulfilled",
                    tint = if (task.isFulfilled) Color.DarkGray else Color.DarkGray
                )

                 */
            }
        }

    }
