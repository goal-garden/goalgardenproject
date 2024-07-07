package com.example.goal_garden_project.screens

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.models.Task
import com.example.goal_garden_project.permissionHandler.rememberNotificationPermissionLauncher
import com.example.goal_garden_project.viewmodels.AddTaskViewModel
import com.example.goal_garden_project.viewmodels.AddTaskViewModelFactory
import com.example.goal_garden_project.widgets.SimpleTopBar
import com.example.goal_garden_project.viewmodels.DetailViewModel
import com.example.goal_garden_project.viewmodels.DetailViewModelFactory
import com.example.goal_garden_project.widgets.ReminderIntervalDropdown
import com.example.goal_garden_project.widgets.TaskList
import com.example.goal_garden_project.widgets.TimePickerButton
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EditScreen(goalId: Long, navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(LocalContext.current)
    val repository = GoalRepository(db.goalDao())
    val factory = DetailViewModelFactory(repository)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    val taskRepository = TaskRepository(taskDao = db.taskDao())
    val factory2 = AddTaskViewModelFactory(repository = repository, repository2 = taskRepository)
    val viewModel2: AddTaskViewModel = viewModel(factory = factory2)

    LaunchedEffect(key1 = goalId) {
        viewModel.getGoalById(goalId)
    }

    val specificGoal by viewModel.specificGoal.collectAsState()
    var isReminderSet by remember {  mutableStateOf( specificGoal?.goal?.reminderOn?:false)}

    // New state variables for time and interval
    var notificationHour by remember { mutableStateOf(specificGoal?.goal?.reminderTime?:0) }
    var notificationMinute by remember { mutableStateOf(specificGoal?.goal?.reminderTime?:0) }
    var selectedInterval by remember { mutableStateOf(specificGoal?.goal?.reminderInterval?:0) }    //das ist eigentlich ein string
    var notificationInterval by remember { mutableStateOf(specificGoal?.goal?.reminderInterval?:0)}

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isFulfilled by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(0) }
    var tasks by remember { mutableStateOf(mutableStateListOf<Task>()) }
    var showDialog by remember { mutableStateOf(false) }
    var prepreparetasks = remember { mutableStateListOf<Task>()}

    val notificationPermissionLauncher = rememberNotificationPermissionLauncher()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(specificGoal) {
        specificGoal?.let {
            title = it.goal.title
            description = it.goal.description
            isFulfilled = it.goal.isFulfilled
            date = it.goal.date
            tasks.clear() // Clear existing tasks
            tasks.addAll(it.tasks)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopBar(text = "Edit Goal", true, navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            specificGoal?.let {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = date.toString(),
                        onValueChange = { date = it.toIntOrNull() ?: 0 },
                        label = { Text("Date") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Completion status: ",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Checkbox(
                            checked = isFulfilled,
                            onCheckedChange = { isFulfilled = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Set Reminder", modifier = Modifier.weight(1f))
                        Switch(
                            checked = isReminderSet,
                            onCheckedChange = { isReminderSet = it
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
                                }}
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isReminderSet){
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
                        /*
                        ReminderIntervalDropdown(
                            selectedInterval = selectedInterval,
                            onIntervalSelected = { interval, value ->
                                selectedInterval = interval
                                notificationInterval = value
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                         */

                    }
                    Spacer(modifier = Modifier.height(16.dp))
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(Modifier.height(200.dp)){
                        TaskList(tasks+prepreparetasks,)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.updateGoal(goalId, title, description, date, isFulfilled)

                            viewModel2.addTasks(goalId, prepreparetasks)


                            navController.navigateUp()
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Save"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save")
                    }
                }
            } ?: run {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
