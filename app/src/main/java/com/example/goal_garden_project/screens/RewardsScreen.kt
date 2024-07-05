package com.example.goal_garden_project.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.ui.theme.CustomGreen
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.viewmodels.TaskViewModel
import com.example.goal_garden_project.viewmodels.TaskViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar
import com.example.goal_garden_project.widgets.SimpleTopBar

@Composable
fun RewardsScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val taskRepository = TaskRepository(taskDao = db.taskDao())
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val pictureRepository = PictureRepository(pictureDao = db.pictureDao())
    val taskFactory = TaskViewModelFactory(
        repository = taskRepository,
        repository2 = goalRepository,
        repository3 = pictureRepository
    )
    val goalFactory = GoalViewModelFactory(repository = goalRepository)
    val goalViewModel: GoalViewModel = viewModel(factory = goalFactory)
    val taskViewModel: TaskViewModel = viewModel(factory = taskFactory)


    val completedGoals by goalViewModel.finishedGoals.collectAsState()
    val allTasks by taskViewModel.tasks.collectAsState()
    val completedTasks = allTasks.filter { it.isFulfilled }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Box {
                SimpleTopBar(
                    text = "Achievements",
                    navController = navController
                )
                Icon(
                    imageVector = Icons.TwoTone.Star,
                    contentDescription = "rewards",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 70.dp)
                )
                Icon(
                    imageVector = Icons.TwoTone.Star,
                    contentDescription = "rewards",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 70.dp)
                )

            }
        },
        bottomBar = {
            SimpleBottomBar(navController, Screen.Add.route)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            val taskMilestones = listOf(
                5, 10, 20, 40, 50
            )
            val goalMilestones = listOf(
                5, 10, 20, 50
            )

            goalMilestones.forEach { finalNumber ->
                val progress = (completedGoals.count().toFloat() / finalNumber * 100).coerceAtMost(100f)
                AchievementProgress(
                    title = "$finalNumber Goal Milestone",
                    progress = progress
                )
            }
            taskMilestones.forEach { finalNumber ->
                val progress = (completedTasks.count().toFloat() / finalNumber * 100).coerceAtMost(100f)
                AchievementProgress(
                    title = "$finalNumber Task Milestone",
                    progress = progress
                )
            }
        }
    }
}

@Composable
fun AchievementProgress(title: String, progress: Float) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progress / 100,
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .padding(start = 10.dp, end = 10.dp),
            trackColor = Color.LightGray,
            color = CustomGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${progress.toInt()}%",
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RewardScreenPreview() {
    RewardsScreen(navController = rememberNavController())
}