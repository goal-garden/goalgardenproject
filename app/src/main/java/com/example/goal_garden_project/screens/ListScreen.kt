package com.example.goal_garden_project.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar


@Composable
fun ListScreen(navController: NavController) {//, moviesViewModel: HomeViewModel) {

//    val db = AppDatabase.getDatabase(LocalContext.current)
//    val repository = Repository(movieDao = db.movieDao())
//    val factory = GoalViewModelFactory()//repository = repository)
//    val viewModel: GoalViewModel = viewModel(factory = factory)

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
            Text(
                text = "List",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}



