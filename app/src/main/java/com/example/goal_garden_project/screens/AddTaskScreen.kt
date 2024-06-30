package com.example.goal_garden_project.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.Task
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.viewmodels.AddViewModel
import com.example.goal_garden_project.viewmodels.AddViewModelFactory
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.viewmodels.TaskViewModel
import com.example.goal_garden_project.viewmodels.TaskViewModelFactory
import com.example.goal_garden_project.widgets.PlantDropdownMenu
import com.example.goal_garden_project.widgets.SimpleTopBar
import kotlinx.coroutines.launch
import java.util.Date


@Composable
fun AddTaskScreen(navController: NavController) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val factory = GoalViewModelFactory(repository = goalRepository)
    val viewModel: GoalViewModel = viewModel(factory = factory)
    val taskRepository = TaskRepository(taskDao = db.taskDao())
    val factory2 = TaskViewModelFactory(repository = taskRepository)
    val viewModel2: TaskViewModel = viewModel(factory = factory2)
    val coroutineScope = rememberCoroutineScope()

    val goalsWithPlantPicture by viewModel.goalsWithImageAndTitle.collectAsState()

    var goalId by remember { mutableStateOf("") }
    var plantName by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopBar("Add Task", true, navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                PlantDropdownMenu(
                    context = context,
                    goalsWithPlantPicture = goalsWithPlantPicture,
                    plantName = plantName,
                    onPlantSelected = { id, name ->
                        goalId = id
                        plantName = name
                    }
                )

                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.shapes.small
                                )
                                .padding(16.dp)
                        ) {
                            if (title.isEmpty()) Text("Title")
                            innerTextField()
                        }
                    }
                )

                BasicTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.shapes.small
                                )
                                .padding(16.dp)
                        ) {
                            if (description.isEmpty()) Text("Description")
                            innerTextField()
                        }
                    }
                )

                Button(
                    onClick = {
                        val task = Task(
                            goalId = goalId.toLong(),
                            name = title,
                            description = description,
                            date = Date().time.toInt(), // Default value for now
                            isFulfilled = false
                        )
                        coroutineScope.launch {
                            viewModel2.addTask(task)

                            Toast.makeText(context, "Task added", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text("Add Task")
                }
            }
        }
    }
}
