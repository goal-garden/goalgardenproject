package com.example.goal_garden_project.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen (val route: String, val title: String, val icon: ImageVector){
    data object Home : Screen("homescreen", "Home", Icons.Default.Home)
    data object Plant : Screen("plantscreen/{plantId}", "Plant", Icons.Default.Favorite)
    data object Detail : Screen("detailscreen/{plantId}", "Detail", Icons.Default.Info)
    data object Goal : Screen("goalscreen", "Goal", Icons.Default.Star)
    data object List : Screen("listscreen", "List", Icons.Default.List)
    data object Add : Screen("addscreen", "Add", Icons.Default.Add)
    data object Task : Screen("taskscreen", "Task", Icons.Default.Done)





}