package com.example.goal_garden_project.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.R
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar


@Composable
fun HomeScreen(navController: NavController) {//, moviesViewModel: HomeViewModel) {

    val db = AppDatabase.getDatabase(LocalContext.current)
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val plantRepository = PlantRepository(plantDao = db.plantDao())
    val pictureRepository = PictureRepository(pictureDao = db.pictureDao())
    val factory = GoalViewModelFactory(repository = goalRepository)
    val viewModel: GoalViewModel = viewModel(factory = factory)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Collecting the images from the repository should be done by the view model
    val imageListFlow =
        goalRepository.getAllGoalImages() // Assuming this returns Flow<List<Picture>>
    val imageList by imageListFlow.collectAsState(initial = emptyList())


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            SimpleBottomBar(navController)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


            val chunkedImages = imageList.chunked(3)
            val rowsToShow = 4

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Iterate over a range to ensure at least 4 rows are displayed
                itemsIndexed((0 until rowsToShow).toList()) { index, _ ->
                    // Check if there are images for the current row index
                    val rowImages = chunkedImages.getOrNull(index)
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowImages?.forEach { image ->
                                val imageResourceId = context.resources.getIdentifier(
                                    image.imageUrl, "drawable", context.packageName
                                )
                                Image(
                                    painter = painterResource(id = imageResourceId),
                                    contentDescription = image.imageUrl,
                                    modifier = Modifier
                                        .size(130.dp)
                                        .padding(5.dp)
                                        .clickable {
                                            // Navigate to another destination

                                            navController.navigate(
                                                Screen.Plant.route.replace(
                                                    "{goalId}",
                                                    image.goalId.toString()
                                                )
                                            )
                                        },
                                    contentScale = ContentScale.Crop

                                )
                            }
                            // Add empty boxes to fill the row if it's not complete
                            if (rowImages == null) {
                                Box(
                                    modifier = Modifier
                                        .size(130.dp)
                                        .padding(5.dp),
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 10.dp, vertical = 15.dp
                                )
                                .height(4.dp)
                                .background(Color.DarkGray),
                        )
                    }
                }
            }


        }
    }
}



