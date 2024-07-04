package com.example.goal_garden_project.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.goal_garden_project.R
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.models.GoalWithTasks
import com.example.goal_garden_project.models.Task
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.ui.theme.CustomYellow
import com.example.goal_garden_project.widgets.SimpleTopBar
import com.example.goal_garden_project.viewmodels.DetailViewModel
import com.example.goal_garden_project.viewmodels.DetailViewModelFactory
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailScreen(goalId: Long, navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val factory = DetailViewModelFactory(repository = goalRepository)
    val viewModel: DetailViewModel = viewModel(factory = factory)


    LaunchedEffect(key1 = goalId) {
        viewModel.getGoalById(goalId)
        viewModel.getGoalByIdWithPicture(goalId)
    }

    val goalWithTasks by viewModel.specificGoal.collectAsState()
    val goalWithPlantPicture by viewModel.goalWithPlantPicture.collectAsState()
    val imageUrl = goalWithPlantPicture?.imageUrl
    val imageResource = if (imageUrl != null) {
        context.resources.getIdentifier(imageUrl, "drawable", context.packageName)
    } else {
        R.drawable.sonnenb5 // Replace with your default drawable resource
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            SimpleTopBar(
                text = goalWithTasks?.goal?.title ?: "Loading...",
                backButton = true,
                navController = navController
            )
        },

        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            goalWithTasks?.let { goalWithTasks ->
                Column(modifier = Modifier.padding(16.dp)) {
                    GoalCard(goalWithTasks, imageResource)
                    Text(text = "Tasks", modifier = Modifier.padding(5.dp), fontSize = 18.sp)
                    TaskList(goalWithTasks.tasks)
                    Row {
                        ActionButton(text = "Edit", icon = Icons.Filled.Edit, color = CustomYellow)
                        ActionButton(text = "Delete", icon = Icons.Filled.Close, color = Color.Red)
                    }

                }
            } ?: run {
                Text(text = "Loading...", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun GoalCard(goalWithTasks: GoalWithTasks, imageResourceId: Int) {
    Row(
        modifier = Modifier
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Card with image and text
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageResourceId),
                    contentDescription = "Goal Image",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Description: ${goalWithTasks.goal.description}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Status: ${if (goalWithTasks.goal.isFulfilled) "Completed" else "In Progress"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }

        // Share button
        IconButton(
            onClick = {
                // Handle share action
            },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share"
            )

        }
    }
}

@Composable
fun TaskList(tasks: List<Task>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        tasks.forEach { task ->
            TaskCard(task)
        }
    }
}

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = task.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector, color: Color = LocalContentColor.current) {
    FilledTonalButton(
        onClick = {
            // Handle edit action
        },
        modifier = Modifier.padding(end = 16.dp),
    ) {
        Text(text = text, modifier = Modifier.padding(end = 2.dp), fontSize = 18.sp, color = color)
        Icon(imageVector = icon, contentDescription = text)
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    DetailScreen(goalId = 1, navController = rememberNavController())
}

