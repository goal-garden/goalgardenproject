@file:Suppress("UNUSED_EXPRESSION")

package com.example.goal_garden_project.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.goal_garden_project.R
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.models.GoalWithTasks
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.ui.theme.CustomYellow
import com.example.goal_garden_project.widgets.SimpleTopBar
import com.example.goal_garden_project.viewmodels.DetailViewModel
import com.example.goal_garden_project.viewmodels.DetailViewModelFactory
import com.example.goal_garden_project.viewmodels.TaskViewModel
import com.example.goal_garden_project.viewmodels.TaskViewModelFactory
import com.example.goal_garden_project.widgets.AchievementProgress
import com.example.goal_garden_project.widgets.ActionButton
import com.example.goal_garden_project.widgets.TaskList
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailScreen(goalId: Long, navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val factory = DetailViewModelFactory(repository = goalRepository)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val taskRepository = TaskRepository(taskDao = db.taskDao())
    val pictureRepository = PictureRepository(pictureDao = db.pictureDao())
    val taskViewModelFactory = TaskViewModelFactory(
        repository = taskRepository,
        repository2 = goalRepository,
        repository3 = pictureRepository
    )
    val taskViewModel: TaskViewModel = viewModel(factory = taskViewModelFactory)


    LaunchedEffect(key1 = goalId) {
        viewModel.getGoalById(goalId)
        viewModel.getGoalByIdWithPicture(goalId)
    }

    val goalWithTasks by viewModel.specificGoal.collectAsState()
    val goalWithPlantPicture by viewModel.goalWithPlantPicture.collectAsState()

    LaunchedEffect(key1 = goalWithPlantPicture) {
        goalWithPlantPicture?.let {
            taskViewModel.getMaxProgressionNumber(plantId = it.plantId)
        }
    }

    val imageUrl = goalWithPlantPicture?.imageUrl
    val imageResource = if (imageUrl != null) {
        context.resources.getIdentifier(imageUrl, "drawable", context.packageName)
    } else {
        R.drawable.sonnenb5 // Replace with your default drawable resource
    }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

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
                    Box(modifier = Modifier.width(300.dp)) {
                        val rawMaxPictureNumber: Int? =
                            taskViewModel.maxProgressionNumber.collectAsState().value
                        Log.d("Detail rawNumber ", "$rawMaxPictureNumber")
                        val maxPictureNumber: Int = rawMaxPictureNumber ?: 0
                        // Capture and share GoalCard

                        GoalCard(
                            goalWithTasks,
                            imageResource,
                            (maxPictureNumber)
                        ) { capturedBitmap ->
                            bitmap = capturedBitmap
                        }
                        // Share button
                        IconButton(
                            onClick = {
                                bitmap?.let {
                                    val file = saveBitmapToFile(context, it)
                                    shareImage(context, file)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Share"
                            )
                        }
                    }
                    Text(text = "Tasks", modifier = Modifier.padding(5.dp), fontSize = 18.sp)
                    Box(Modifier.height(250.dp)) {
                        TaskList(goalWithTasks.tasks,)
                    }
                    Spacer(modifier = Modifier.height(26.dp))
                    Row {
                        ActionButton(
                            text = "Edit",
                            icon = Icons.Filled.Edit,
                            color = CustomYellow,
                            onClick = { navController.navigate("editscreen/$goalId") })

                        ActionButton(
                            text = "Delete",
                            icon = Icons.Filled.Close,
                            color = Color.Red,
                            onClick = {
                                showDeleteConfirmation = true
                            })
                    }

                }
            } ?: run {
                Text(text = "Loading...", modifier = Modifier.align(Alignment.Center))
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
                    })
            }
        }
    }
}

@Composable
fun GoalCard(
    goalWithTasks: GoalWithTasks,
    imageResourceId: Int,
    totalPicturesNumber: Int,
    onCapture: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val composeView = remember { ComposeView(context) }
    Log.d("total pic n", "$totalPicturesNumber")
    // Set content of ComposeView
    composeView.setContent {
        Card(
            modifier = Modifier.width(IntrinsicSize.Min),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = goalWithTasks.goal.title,
                    style = MaterialTheme.typography.titleMedium
                )
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
                val progress =
                    (goalWithTasks.goal.progressionStage.toFloat() / totalPicturesNumber * 100).coerceAtMost(
                        100f
                    )
                Log.d("sssss", "${goalWithTasks.goal.progressionStage.toFloat()}")
                AchievementProgress(title = "", progress = progress)
            }
        }
    }

    // Create and return the bitmap
    LaunchedEffect(Unit) {
        val bitmap = Bitmap.createBitmap(
            composeView.width,
            composeView.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)
        composeView.draw(canvas)
        onCapture(bitmap)
    }

    // Add the ComposeView to the layout
    AndroidView(
        factory = { composeView },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
    val file = File(context.cacheDir, "${UUID.randomUUID()}.png")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return file
}

fun shareImage(context: Context, file: File) {
    val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/png"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, null))
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    DetailScreen(goalId = 1, navController = rememberNavController())
}

