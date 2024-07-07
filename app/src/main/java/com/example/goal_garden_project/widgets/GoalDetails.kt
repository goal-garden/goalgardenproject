package com.example.goal_garden_project.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goal_garden_project.models.Task

@Composable
fun TaskList(tasks: List<Task>, onClick: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        tasks.forEach { task ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
            ) {
                TaskCard(task)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 8.dp, top = 8.dp)
                ) {
                    DeleteIconButton(onClick = { onClick(task.taskId) })
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = task.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DeleteIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.padding(start = 5.dp, end = 2.dp)) {
        Icon(Icons.Outlined.Close, contentDescription = "delete")
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    color: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.padding(end = 16.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(end = 2.dp),
            fontSize = 18.sp,
            color = color
        )
        Icon(imageVector = icon, contentDescription = text)
    }
}
