package com.example.goal_garden_project.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.models.Goal

import com.example.goal_garden_project.viewmodels.AddViewModel
import com.example.goal_garden_project.viewmodels.AddViewModelFactory
import com.example.goal_garden_project.widgets.PlantDropdownMenu
import com.example.goal_garden_project.widgets.SimpleTopBar
import kotlinx.coroutines.launch
import java.util.Date


@Composable
fun AddScreen(navController: NavController) {
    val context = LocalContext.current

    val db = AppDatabase.getDatabase(LocalContext.current)

    val repository = GoalRepository(goalDao = db.goalDao())
    val repository2= PlantRepository(plantDao = db.plantDao())
    val factory = AddViewModelFactory(repository = repository, repository2=repository2)  //does Goal viewmodel suffy
    val viewModel: AddViewModel = viewModel(factory = factory)

    var plantId by remember { mutableStateOf("") }
    var plantName by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf("") }

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
                        val goal = Goal(
                            plantId = plantId.toLong(),
                            progressionStage = 0, // Default value for now
                            title = title,
                            description = description,
                            date = Date().time.toInt(), // Default value for now
                            //tasks = tasks,        //later
                            isFulfilled = false
                        )
                        coroutineScope.launch {
                            viewModel.addGoal(goal)

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
