package com.example.goal_garden_project.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.widgets.SimpleTopBar
import com.example.goal_garden_project.viewmodels.DetailViewModel
import com.example.goal_garden_project.viewmodels.DetailViewModelFactory

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailScreen(goalId: Long, navController: NavController){//, moviesViewModel: HomeViewModel) {

    val db = AppDatabase.getDatabase(LocalContext.current)
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val plantRepository = PlantRepository(plantDao = db.plantDao())
    val pictureRepository = PictureRepository(pictureDao = db.pictureDao())
    val factory = DetailViewModelFactory(repository = goalRepository, goalId=goalId)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    val coroutineScope = rememberCoroutineScope()
    val context =  LocalContext.current


    println(viewModel.hello)
    println(viewModel.imageUrl)

    //this doesnt work  and i dont get why
    val imageUrlState by viewModel.imageUrl.collectAsState()
    println("heeeeeeeellllllllllooooooooooo")
    println(imageUrlState)


    Scaffold(
        modifier= Modifier
            .fillMaxSize(),
        topBar = { SimpleTopBar(text = goalId.toString(), backButton = true, navController = navController)},

    ){
            innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

/* später das nehmen für dynamic image::
            Image(
                painter = painterResource(id = context.resources.getIdentifier(
                    imageUrlState, "drawable", context.packageName
            )),

 */
            Image(painter = painterResource(id = R.drawable.sonnenb1),      //nicht dynamisch!!!!!
                contentDescription = "my-goal",
                modifier = Modifier
                    .size(130.dp)
                    .padding(5.dp),
                contentScale = ContentScale.Crop

            )


        }    }
}



