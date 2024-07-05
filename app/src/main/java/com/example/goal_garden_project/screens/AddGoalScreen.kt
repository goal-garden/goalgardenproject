package com.example.goal_garden_project.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.Task
import com.example.goal_garden_project.reminder.NotificationHandler
import com.example.goal_garden_project.viewmodels.AddTaskViewModel
import com.example.goal_garden_project.viewmodels.AddTaskViewModelFactory

import com.example.goal_garden_project.viewmodels.AddViewModel
import com.example.goal_garden_project.viewmodels.AddViewModelFactory
import com.example.goal_garden_project.widgets.PlantDropdownMenu
import com.example.goal_garden_project.widgets.SimpleTopBar
import com.example.goal_garden_project.widgets.TaskList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun AddScreen(navController: NavController) {
    val context = LocalContext.current

    val db = AppDatabase.getDatabase(LocalContext.current)

    val repository = GoalRepository(goalDao = db.goalDao())
    val repository2= PlantRepository(plantDao = db.plantDao())
    val factory = AddViewModelFactory(repository = repository, repository2=repository2)  //does Goal viewmodel suffy
    val viewModel: AddViewModel = viewModel(factory = factory)

    val taskRepository = TaskRepository(taskDao = db.taskDao())
    val factory2 = AddTaskViewModelFactory(repository = repository, repository2 = taskRepository)
    val viewModel2: AddTaskViewModel = viewModel(factory = factory2)

    var plantId by remember { mutableStateOf("") }
    var plantName by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var prepreparetasks = remember { mutableStateListOf<Task>()}
    val notificationHandler = NotificationHandler(context)


    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, send notification
            println("granted")
            notificationHandler.sendNotification("Goal Achieved!", "You have completed your goal.")
        } else {
            println("denied")
            // Permission is denied
            // Optionally handle the case where permission is denied
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val possiblePlants by viewModel.pictures.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopBar("Add Goal", true, navController)
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
                    goalsWithPlantPicture = possiblePlants,
                    plantName = plantName,
                    onPlantSelected = { id, name ->
                        plantId = id
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
                        showDialog = true
                    },
                                               //first color = background, second color = icon color
                    shape = RoundedCornerShape(5)
                ) {
                    Text(
                        text = "add task",
                        textAlign = TextAlign.Center, // Center aligns the text horizontally
                        fontSize = 20.sp, // Increase font size
                        modifier = Modifier
                            .align(Alignment.CenterVertically) // Center aligns vertically
                    )
                    //Icon(imageVector = Icons.Default.Send, contentDescription = "Open Popup", modifier = Modifier.size(36.dp))
                }

                if (showDialog) {
                    AddTaskPopUp(onDismissRequest = { showDialog = false }, prepreparetasks)

                }
                Box(Modifier.height(200.dp)){
                    TaskList(prepreparetasks)
                }
                Button(
                    onClick = {
                        if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            println("send notification")
                            // Permission already granted, send notification
                            notificationHandler.sendNotification("Goal Achieved!", "You have completed your goal.")
                        } else {
                            // Request permission
                            println("request permission")
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Send Notification")
                }


                Button(
                    onClick = {
                        val goal = Goal(
                            plantId = plantId.toLong(),
                            progressionStage = 0, // Default value for now
                            title = title,
                            description = description,
                            date = Date().time.toInt(), // Default value for now

                            isFulfilled = false
                        )
                        coroutineScope.launch {
                            //viewModel.addGoal(goal)
                            /*
                            prepreparetasks.forEach{task ->
                                viewModel2.addTask(task.copy(goalId = newGoalId))

                             */

                            viewModel2.addGoalAndTasks(goal, prepreparetasks)
                            Toast.makeText(context, "Goal added", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text("Add Goal")
                }
            }
        }
    }


}
