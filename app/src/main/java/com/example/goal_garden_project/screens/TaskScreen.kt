package com.example.goal_garden_project.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.widgets.SimpleBottomBar


@Composable
fun TaskScreen(navController: NavController){//, moviesViewModel: HomeViewModel) {

    //val db = MovieDatabase.getDatabase(LocalContext.current)
    //val repository = MovieRepository(movieDao = db.movieDao())
    //val factory = FavoriteViewModelFactory()//repository = repository)
    //val viewModel: FavoriteViewModel = viewModel(factory = factory)

    Scaffold(
        modifier= Modifier
            .fillMaxSize(),
        bottomBar = {
            SimpleBottomBar(navController, Screen.AddTask.route, Color.Magenta)
        },
    ){
            innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Task",
                modifier = Modifier.align(Alignment.Center)
            )
        }    }
}



