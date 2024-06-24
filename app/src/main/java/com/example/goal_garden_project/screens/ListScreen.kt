package com.example.goal_garden_project.screens


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar


@Composable
fun ListScreen(navController: NavController) {

    val db = AppDatabase.getDatabase(LocalContext.current)
    val repository = GoalRepository(goalDao = db.goalDao())
    val factory = GoalViewModelFactory(repository = repository)
    val viewModel: GoalViewModel = viewModel(factory = factory)

    val selectedFilter = remember { mutableStateOf(GoalFilter.NULL) }
    val inProgressGoals by viewModel.unfinishedGoals.collectAsState()
    val completedGoals by viewModel.finishedGoals.collectAsState()
    val allGoals by viewModel.goals.collectAsState()
//    val waitingGoals by viewModel.waitingToBeSeededGoals.collectAsState()


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            SimpleBottomBar(navController)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Title
            Text(
                text = "My Goals",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            // Row with buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            selectedFilter.value = GoalFilter.WAITING_TO_BE_SEEDED
                            Log.d("ListScreen", "Selected filter: ${selectedFilter.value}")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Waiting to be seeded")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            selectedFilter.value = GoalFilter.IN_PROGRESS
                            Log.d("ListScreen", "Selected filter: ${selectedFilter.value}")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("In Progress")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            selectedFilter.value = GoalFilter.COMPLETED
                            Log.d("ListScreen", "Selected filter: ${selectedFilter.value}")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Completed")
                    }
                }
            }
            // List based on selected filter
            when (selectedFilter.value) {
//                GoalFilter.WAITING_TO_BE_SEEDED -> {
//                    GoalList(goals = waitingGoals)
//                }
                GoalFilter.IN_PROGRESS -> {
                    GoalList(goals = inProgressGoals)
                }

                GoalFilter.COMPLETED -> {
                    GoalList(goals = completedGoals)
                }

                else -> {
                    GoalList(goals = allGoals)
                }
            }
        }
    }
}

enum class GoalFilter {
    WAITING_TO_BE_SEEDED,
    IN_PROGRESS,
    COMPLETED,
    NULL
}

@Composable
fun GoalList(goals: List<Goal>) {
    LazyColumn {
        items(goals) { goal ->
            GoalItem(goal = goal)
        }
    }
}

@Composable
fun GoalItem(goal: Goal) {
    Text(text = goal.title)
}




