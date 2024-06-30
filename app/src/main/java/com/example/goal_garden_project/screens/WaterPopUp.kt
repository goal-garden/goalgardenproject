package com.example.goal_garden_project.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.viewmodels.TaskViewModel
import com.example.goal_garden_project.viewmodels.TaskViewModelFactory

@Composable
fun WateringPopup(onDismissRequest: () -> Unit, goalId:Long) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val taskRepository = TaskRepository(taskDao = db.taskDao())
    val factory = TaskViewModelFactory(repository = taskRepository)
    val viewModel: TaskViewModel = viewModel(factory = factory)

    LaunchedEffect(key1 = goalId) {
        viewModel.getUnfinishedTasks(goalId)
    }

    val tasks by viewModel.unfinishedTasks.collectAsState()

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(color = Color.White)
                .size(300.dp)
        ) {
            Text(text = "This is a popup for watering the plant!")
        }
    }
}
