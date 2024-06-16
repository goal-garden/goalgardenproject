package com.example.goal_garden_project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.goal_garden_project.screens.AddScreen
import com.example.goal_garden_project.screens.DetailScreen
import com.example.goal_garden_project.screens.GoalScreen
import com.example.goal_garden_project.screens.HomeScreen
import com.example.goal_garden_project.screens.ListScreen
import com.example.goal_garden_project.screens.TaskScreen

@Composable
fun Navigation() {
    val navController = rememberNavController() // create a NavController instance
    //val moviesViewModel: MoviesViewModel = viewModel()  // create a MoviesViewModel instance to use in HomeScreen and WatchlistScreen

    NavHost(navController = navController, // pass the NavController to NavHost
        startDestination = Screen.Home.route) {  // pass a start destination

        composable(route = Screen.Home.route){   // route with name "homescreen" navigates to HomeScreen composable
            HomeScreen(navController = navController)//, //moviesViewModel=moviesViewModel)
        }
        composable(route = Screen.Goal.route){
            GoalScreen(navController = navController)//, moviesViewModel)
        }
        composable(route = Screen.Add.route){   // route with name "homescreen" navigates to HomeScreen composable
            AddScreen(navController = navController)//, //moviesViewModel=moviesViewModel)
        }
        composable(route = Screen.Task.route){
            TaskScreen(navController = navController)//, moviesViewModel)
        }
        composable(route = Screen.List.route){
            ListScreen(navController = navController)//, moviesViewModel)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument(name = "goalId") {type = NavType.StringType})
        ) { backStackEntry ->
            DetailScreen(goalId = backStackEntry.arguments?.getString("movieId")?.toLong() ?: 0, navController)
        }

    }
}

