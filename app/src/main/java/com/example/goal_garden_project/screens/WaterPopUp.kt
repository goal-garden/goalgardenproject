package com.example.goal_garden_project.screens


import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.viewmodels.TaskViewModel
import com.example.goal_garden_project.viewmodels.TaskViewModelFactory
import com.example.goal_garden_project.widgets.TaskList


@OptIn(ExperimentalMaterial3Api::class)
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


    AlertDialog(
        onDismissRequest = onDismissRequest, properties = DialogProperties(
            usePlatformDefaultWidth = false

        ),
    ) {
        Box(
            modifier = Modifier
                //.padding(start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(10.dp)) // Apply rounded corners
                .background(color = Color.White)
                .height(550.dp)
                .width(300.dp),

            )
        {
            Column(Modifier.padding(15.dp)) {
                Text(text = "select fulfilled task:", style = MaterialTheme.typography.headlineMedium)
                TaskList(tasks) //on click change color
            }

        }

        //this is how you set a alignment of a dialog!!!!???!?!?
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.RIGHT)
    }

}