package com.example.goal_garden_project.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.viewmodels.DetailViewModel
import com.example.goal_garden_project.viewmodels.DetailViewModelFactory
import com.example.goal_garden_project.widgets.SimpleTopBar
import com.example.goal_garden_project.widgets.TaskList

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailScreen(goalId: Long, navController: NavController) {
    val db = AppDatabase.getDatabase(LocalContext.current)
    val repository = GoalRepository(db.goalDao())
    val factory = DetailViewModelFactory(repository)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    // Fetch the goal details when the screen is composed
    LaunchedEffect(key1 = goalId) {
        viewModel.getGoalById(goalId)
    }

    val specificGoal by viewModel.specificGoal.collectAsState()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopBar(
                text = specificGoal?.goal?.title ?: "Goal Details",
                true,
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            specificGoal?.let { goalWithTasks ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header with image and title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = goalWithTasks.goal.title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Description
                    Text(
                        text = goalWithTasks.goal.description,
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Completion status
                    Text(
                        text = "Completion status: ${if (goalWithTasks.goal.isFulfilled) "Yes" else "No"}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (goalWithTasks.goal.isFulfilled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Tasks
                    Text(
                        text = "Tasks:",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Box(Modifier.height(500.dp)) {
                            TaskList(goalWithTasks.tasks)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                navController.navigate("editscreen/$goalId")
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit", color = Color.White)
                        }

                        Button(
                            onClick = {
                                showDeleteConfirmation = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Delete", color = Color.White)
                        }
                    }
                }
            } ?: run {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // CircularProgressIndicator removed to avoid the error
                }
            }

            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteConfirmation = false
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
                                viewModel.deleteGoal(goalId)
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) {
                                        inclusive = true
                                    }
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
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
