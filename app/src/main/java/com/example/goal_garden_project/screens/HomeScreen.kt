package com.example.goal_garden_project.screens


import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Modifier
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
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController){//, moviesViewModel: HomeViewModel) {

    val db = AppDatabase.getDatabase(LocalContext.current)
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val plantRepository = PlantRepository(plantDao = db.plantDao())
    val pictureRepository = PictureRepository(pictureDao = db.pictureDao())
    val factory = GoalViewModelFactory()//repository = repository)
    val viewModel: GoalViewModel = viewModel(factory = factory)
    val coroutineScope = rememberCoroutineScope()
    val context =  LocalContext.current

    // Collecting the images from the repository
    val imageListFlow = goalRepository.getAllGoalImages() // Assuming this returns Flow<List<Picture>>
    val imageList by imageListFlow.collectAsState(initial = emptyList())


    Scaffold(
        modifier= Modifier
            .fillMaxSize(),
        bottomBar = {
            SimpleBottomBar(navController)
        },
    ){
            innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                content = {
                    items(imageList) { image ->
                        val imageResourceId =  context.resources.getIdentifier(image.imageUrl, "drawable", context.packageName)
                        Image(
                            painter = painterResource(id = imageResourceId),
                            contentDescription = image.imageUrl,
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Crop
                        )


                    }


                }
            )


        }    }
}



