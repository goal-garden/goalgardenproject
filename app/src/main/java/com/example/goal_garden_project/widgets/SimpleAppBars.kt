package com.example.goal_garden_project.widgets


import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.goal_garden_project.navigation.Screen

@Composable
@OptIn(ExperimentalMaterial3Api::class)     //das braucht man für die topbar, macht der typ im youtubevideo auch (von den bereitgestellten resourcen)
fun SimpleTopBar(text:String?, backButton:Boolean = false, navController:NavController? =null) {      //eigenen funktion mainly for readability
    CenterAlignedTopAppBar(
        title = {
            Text(text?:"")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (backButton){
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        }}
        ,
    )
}

@Composable
fun SimpleBottomBar(navController: NavController){    //eigenen funktion mainly for readability, kann man aber auch nochmal gebrauchen wenn man eine bottom bar mit den gleichen symbolen hat
    val screens = listOf(
        Screen.Task,
        Screen.List,
        Screen.Home,
        Screen.Goal
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        IconsAdder(screens, currentDestination, navController)
    }

    //TODO: add add-button


}


@Composable
fun RowScope.IconsAdder(screens: List<Screen>, currentDestination: NavDestination?,
                        navController: NavController){      //extra damit ichs schnell auch in andere app gebrauchen kann, oder wenn ich wieder ne liste an icons erstellen muss
    screens.forEach { screen -> (
            NavigationBarItem( selected = currentDestination?.hierarchy?.any {
                it.route==screen.route
            } ==true,
                onClick = {navController.navigate(screen.route){
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true      //damits keine copies von der selben page gibt
                } },
                label = {Text(text = screen.title)},
                icon = {Icon(
                    imageVector = screen.icon,
                    contentDescription = screen.title
                )}
            ))
    }
}


