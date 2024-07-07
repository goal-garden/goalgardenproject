package com.example.goal_garden_project.screens

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.permissionHandler.rememberNotificationPermissionLauncher

import com.example.goal_garden_project.data.repositories.GoalRepository
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
import com.example.goal_garden_project.widgets.ReminderIntervalDropdown
import com.example.goal_garden_project.widgets.SimpleTopBar
import com.example.goal_garden_project.widgets.TaskList
import com.example.goal_garden_project.widgets.TimePickerButton
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@SuppressLint("CoroutineCreationDuringComposition")

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AddScreen(navController: NavController) {
    val context = LocalContext.current

    val db = AppDatabase.getDatabase(LocalContext.current)

    val repository = GoalRepository(goalDao = db.goalDao())
    val repository2 = PlantRepository(plantDao = db.plantDao())
    val factory = AddViewModelFactory(repository = repository, repository2 = repository2)
    val viewModel: AddViewModel = viewModel(factory = factory)

    val taskRepository = TaskRepository(taskDao = db.taskDao())
    val factory2 = AddTaskViewModelFactory(repository = repository, repository2 = taskRepository)
    val addTaskViewModel: AddTaskViewModel = viewModel(factory = factory2)

    var plantId by remember { mutableStateOf("") }
    var plantName by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var showTaskDialog by remember { mutableStateOf(false) }
    var prepreparetasks = remember { mutableStateListOf<Task>() }
    val notificationHandler = NotificationHandler(context)

    // New state variables for time and interval
    var notificationHour by remember { mutableStateOf(0L) }
    var notificationMinute by remember { mutableStateOf(0L) }

    // State for the toggle button
    var isReminderSet by remember { mutableStateOf(false) }
    var notificationInterval by remember { mutableStateOf(AlarmManager.INTERVAL_DAY) }

    var seedlater by remember { mutableStateOf(false) }
    val notificationPermissionLauncher = rememberNotificationPermissionLauncher()

    val coroutineScope = rememberCoroutineScope()

    val possiblePlants by viewModel.pictures.collectAsState()
    //val goalId by viewModel.goalId.collectAsState()

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
                .verticalScroll(rememberScrollState())
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Set Reminder", modifier = Modifier.weight(1f))
                    Switch(
                        checked = isReminderSet,
                        onCheckedChange = {
                            isReminderSet = it
                            coroutineScope.launch {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        "android.permission.POST_NOTIFICATIONS"
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    println("postnotification granted")
                                } else {
                                    notificationPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                                }
                            }
                        }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Seed Later", modifier = Modifier.weight(1f))
                    Switch(
                        checked = seedlater,
                        onCheckedChange = {
                            seedlater = it
                        }
                    )
                }

                // Time Picker
                if (isReminderSet) {
                    TimePickerButton(
                        context = context,
                        notificationHour = notificationHour,
                        notificationMinute = notificationMinute,
                        onTimeSelected = { hour, minute ->
                            notificationHour = hour
                            notificationMinute = minute
                        },
                        modifier = Modifier     //add styling here if you want
                    )
                    ReminderIntervalDropdown(
                        initValue = notificationInterval,
                        onIntervalSelected = { value ->
                            notificationInterval = value
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        showTaskDialog = true
                    },
                ) {
                    Text(
                        text = "Add Task",

                        )
                }
                if (showTaskDialog) {
                    AddTaskPopUp(onDismissRequest = { showTaskDialog = false }, prepreparetasks)
                }

                Box(Modifier.height(200.dp)) {
                    TaskList(prepreparetasks)
                }

                //________________________-SAVE-__________________________________
                Button(
                    onClick = {
                        val goal = Goal(
                            plantId = plantId.toLong(),
                            progressionStage = 0,
                            title = title,
                            description = description,
                            date = Date().time,
                            isFulfilled = false,
                            isSeeded = !seedlater,
                            reminderOn = isReminderSet,
                            reminderTime = (notificationHour*360*1000)+(notificationMinute*60*1000),
                            reminderInterval = notificationInterval
                        )
                        coroutineScope.launch {
                            val goalId = viewModel.addGoal(goal)

                            Toast.makeText(context, "Goal added", Toast.LENGTH_SHORT).show()
                            if (prepreparetasks.isNotEmpty()) {
                                addTaskViewModel.addTasks(goalId, prepreparetasks)
                            }
                            if (isReminderSet) {
                                // Calculate the time in milliseconds
                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = System.currentTimeMillis()
                                    set(Calendar.HOUR_OF_DAY, notificationHour.toInt())
                                    set(Calendar.MINUTE, notificationMinute.toInt())
                                    set(Calendar.SECOND, 0)
                                }
                                val notificationTime = calendar.timeInMillis

                                // Schedule the notification
                                notificationHandler.scheduleNotification(
                                    goalId,
                                    notificationInterval,
                                    notificationTime
                                )
                            }


                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text(
                        "Add Goal",
                        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}


