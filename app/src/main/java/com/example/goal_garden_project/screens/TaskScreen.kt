package com.example.goal_garden_project.screens

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.viewmodels.TaskViewModel
import com.example.goal_garden_project.viewmodels.TaskViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar
import com.example.goal_garden_project.widgets.TaskList

@Composable
fun TaskScreen(navController: NavController) {


    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val taskRepository = TaskRepository(taskDao = db.taskDao())
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val pictureRepository = PictureRepository(pictureDao = db.pictureDao())
    val factory = TaskViewModelFactory(
        repository = taskRepository,
        repository2 = goalRepository,
        repository3 = pictureRepository
    )
    val viewModel: TaskViewModel = viewModel(factory = factory)

    val tasks by viewModel.tasks.collectAsState()
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var taskIdToDelete by remember { mutableStateOf<Long?>(null) }

    var filter by remember { mutableStateOf("All") }
    val filteredTasks = when (filter) {
        "Fulfilled" -> tasks.filter { it.isFulfilled }
        "Unfulfilled" -> tasks.filter { !it.isFulfilled }
        else -> tasks.filter { !it.isFulfilled } //tasks - if you want to have all tasks
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
            TaskList(filteredTasks, onClick = { taskId ->
                taskIdToDelete = taskId
                showDeleteConfirmation = true
            })
            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteConfirmation = false
                        taskIdToDelete = null
                    },
                    title = {
                        Text(text = "Confirm Delete")
                    },
                    text = {
                        Text("Are you sure you want to delete this goal?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                taskIdToDelete?.let { taskId ->
                                    viewModel.deleteTaskById(taskId)
                                    showDeleteConfirmation = false
                                    taskIdToDelete = null
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDeleteConfirmation = false
                                taskIdToDelete = null
                            }
                        ) {
                            Text("Cancel")
                        }
                    })
            }
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
                containerColor = if (currentFilter == "Unfulfilled") MaterialTheme.colorScheme.primaryContainer else Color.LightGray,
                contentColor = Color.DarkGray
            )
        ) {
            Text(text = "Unfulfilled")
        }
        Button(
            onClick = { onFilterSelected("Fulfilled") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (currentFilter == "Fulfilled") MaterialTheme.colorScheme.primaryContainer else Color.LightGray,
                contentColor = Color.DarkGray
            )
        ) {
            Text(text = "Fulfilled")
        }

    }
}


