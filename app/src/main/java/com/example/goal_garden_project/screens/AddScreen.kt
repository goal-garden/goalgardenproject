package com.example.goal_garden_project.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.viewmodels.AddViewModel
import com.example.goal_garden_project.viewmodels.AddViewModelFactory
import com.example.goal_garden_project.viewmodels.AppDatabase

import com.example.goal_garden_project.viewmodels.GoalRepository

import com.example.goal_garden_project.widgets.SimpleTopBar
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun AddScreen(navController: NavController) {
    val context = LocalContext.current

    val db = AppDatabase.getDatabase(LocalContext.current)
    val repository = GoalRepository(goalDao = db.goalDao())
    val factory = AddViewModelFactory(repository = repository)  //does Goal viewmodel suffy
    val viewModel: AddViewModel = viewModel(factory = factory)


    var goalId by remember { mutableStateOf("") }
    var plantId by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

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
                Text(text = "Add Goal", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

                BasicTextField(
                    value = goalId,
                    onValueChange = { goalId = it },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                                .padding(16.dp)
                        ) {
                            if (goalId.isEmpty()) Text("Goal ID")
                            innerTextField()
                        }
                    }
                )

                BasicTextField(
                    value = plantId,
                    onValueChange = { plantId = it },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                                .padding(16.dp)
                        ) {
                            if (plantId.isEmpty()) Text("Plant ID")
                            innerTextField()
                        }
                    }
                )

                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
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
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                                .padding(16.dp)
                        ) {
                            if (description.isEmpty()) Text("Description")
                            innerTextField()
                        }
                    }
                )

                BasicTextField(
                    value = tasks,
                    onValueChange = { tasks = it },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                                .padding(16.dp)
                        ) {
                            if (tasks.isEmpty()) Text("Tasks")
                            innerTextField()
                        }
                    }
                )

                Button(
                    onClick = {
                        val goal = Goal(
                            goalId = goalId,
                            plantId = plantId,
                            currentProgressionImageNumber = 0, // Default value for now
                            title = title,
                            description = description,
                            date = Date(), // Default value for now
                            tasks = tasks,
                            isFulfilled = false
                        )
                        coroutineScope.launch {
                            viewModel.addGoal(goal)
                            Toast.makeText(context, "Goal added", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
                ) {
                    Text("Add Goal")
                }
            }
        }
    }
}