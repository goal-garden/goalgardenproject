package com.example.goal_garden_project.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.models.Task
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.viewmodels.TaskViewModel
import com.example.goal_garden_project.viewmodels.TaskViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar

@Composable
fun TaskScreen(navController: NavController) {


    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val taskRepository = TaskRepository(taskDao = db.taskDao())
    val factory = TaskViewModelFactory(repository = taskRepository)
    val viewModel: TaskViewModel = viewModel(factory = factory)

    val tasks by viewModel.tasks.collectAsState()


    var filter by remember { mutableStateOf("All") }
    val filteredTasks = when (filter) {
        "Fulfilled" -> tasks.filter { it.isFulfilled }
        "Unfulfilled" -> tasks.filter { !it.isFulfilled }
        else -> tasks.filter { !it.isFulfilled} //tasks - if you want to have all tasks
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            SimpleBottomBar(navController, Screen.AddTask.route, Color.Magenta)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            FilterButtons(filter) { selectedFilter ->
                filter = selectedFilter
            }
            TaskList(filteredTasks)
        }
    }
}

@Composable
fun FilterButtons(currentFilter: String, onFilterSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { onFilterSelected("Unfulfilled") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (currentFilter == "Unfulfilled") Color.Gray else Color.LightGray
            )
        ) {
            Text(text = "Unfulfilled")
        }
        Button(
            onClick = { onFilterSelected("Fulfilled") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (currentFilter == "Fulfilled") Color.Gray else Color.LightGray
            )
        ) {
            Text(text = "Fulfilled")
        }

    }
}

@Composable
fun TaskList(tasks: List<Task>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks) { task ->
            TaskItem(task)
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
