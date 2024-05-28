package com.example.goal_garden_project.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.viewmodels.FavoriteViewModel
import com.example.goal_garden_project.viewmodels.FavoriteViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar
import com.example.goal_garden_project.widgets.SimpleTopBar


@Composable
fun AddScreen(navController: NavController){//, moviesViewModel: HomeViewModel) {

    //val db = MovieDatabase.getDatabase(LocalContext.current)
    //val repository = MovieRepository(movieDao = db.movieDao())
    val factory = FavoriteViewModelFactory()//repository = repository)
    val viewModel: FavoriteViewModel = viewModel(factory = factory)

    Scaffold(
        modifier= Modifier
            .fillMaxSize(),
        topBar = {
            SimpleTopBar("add", true, navController)
        },
    ){
            innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Add",
                modifier = Modifier.align(Alignment.Center)
            )
        }    }
}



