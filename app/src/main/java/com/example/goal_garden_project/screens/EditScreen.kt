package com.example.goal_garden_project.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.widgets.SimpleTopBar
import com.example.goal_garden_project.viewmodels.DetailViewModel
import com.example.goal_garden_project.viewmodels.DetailViewModelFactory

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EditScreen(goalId: Long, navController: NavController) {
    val db = AppDatabase.getDatabase(LocalContext.current)
    val repository = GoalRepository(db.goalDao())
    val factory = DetailViewModelFactory(repository)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    LaunchedEffect(key1 = goalId) {
        viewModel.getGoalById(goalId)
    }

    val specificGoal by viewModel.specificGoal.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isFulfilled by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(0) }

    LaunchedEffect(specificGoal) {
        specificGoal?.let {
            title = it.goal.title
            description = it.goal.description
            isFulfilled = it.goal.isFulfilled
            date = it.goal.date
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
                    Button(
                        onClick = {
                            viewModel.updateGoal(goalId, title, description, date, isFulfilled)
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
