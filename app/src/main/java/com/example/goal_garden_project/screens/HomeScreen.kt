package com.example.goal_garden_project.screens


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar


@SuppressLint("DiscouragedApi")
@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val factory = GoalViewModelFactory(repository = goalRepository)
    val viewModel: GoalViewModel = viewModel(factory = factory)
    val coroutineScope = rememberCoroutineScope()

    val goalsWithPlantPicture by viewModel.goalsWithPlantPicture.collectAsState()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            SimpleBottomBar(navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Iterate through each row (you want 4 rows)
                items((goalsWithPlantPicture.chunked(3) + List(4 - goalsWithPlantPicture.size / 3) { emptyList() })) { rowItems ->
                    // Each chunk represents a row with 3 items or fewer (last row might have less than 3 items)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Iterate through the 3 items in the row
                        for (goalWithPlantPicture in rowItems) {
                            val imageUrl = goalWithPlantPicture.imageUrl

                            val imageResourceId = context.resources.getIdentifier(
                                imageUrl, "drawable", context.packageName
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = imageResourceId),
                                    contentDescription = imageUrl,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            navController.navigate(
                                                Screen.Plant.route.replace(
                                                    "{goalId}",
                                                    goalWithPlantPicture.goalId.toString()
                                                )
                                            )
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        // Fill empty boxes if rowItems count is less than 3
                        repeat(3 - rowItems.size) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        }
                    }

                    // Add horizontal line between rows
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 15.dp)
                            .height(4.dp)
                            .background(Color.DarkGray),
                    )
                }
            }
        }
    }
}

//Scaffold(
//modifier = Modifier
//.fillMaxSize(),
//modifier = Modifier.fillMaxSize(),
//bottomBar = {
//    SimpleBottomBar(navController)
//},
//}
//) { innerPadding ->
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(innerPadding)
//    ) {
//
//
//        val chunkedImages = imageList.chunked(3)
//        val rowsToShow = 4
//
//        LazyColumn(
//            modifier = Modifier.fillMaxSize()
//                    modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            // Iterate over a range to ensure at least 4 rows are displayed
//            itemsIndexed((0 until rowsToShow).toList()) { index, _ ->
//                // Check if there are images for the current row index
//                val rowImages = chunkedImages.getOrNull(index)
//                Column {
//                    Row(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        rowImages?.forEach { image ->
//                            val imageResourceId = context.resources.getIdentifier(
//                                image.imageUrl, "drawable", context.packageName
//                            )
//                            Image(
//                                painter = painterResource(id = imageResourceId),
//                                contentDescription = image.imageUrl,
//                                modifier = Modifier
//                                    .size(130.dp)
//                                    .padding(5.dp)
//                                    .clickable {
//                                        // Navigate to another destination
//
//                                        navController.navigate(
//                                            Screen.Plant.route.replace(
//                                                "{goalId}",
//                                                image.goalId.toString()
//                                            )
//                                                    itemsIndexed(goalsWithPlantPicture.chunked(3)) { rowIndex, rowItems ->
//                                                Row(
//                                                    modifier = Modifier.fillMaxWidth(),
//                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                                                ) {
//                                                    rowItems.forEach { goalWithPlantPicture ->
//                                                        val imageUrl = goalWithPlantPicture.picture?.imageUrl ?: ""
//                                                        val imageResourceId = context.resources.getIdentifier(
//                                                            imageUrl, "drawable", context.packageName
//                                                        )
//                                                        Image(
//                                                            painter = painterResource(id = imageResourceId),
//                                                            contentDescription = imageUrl,
//                                                            modifier = Modifier
//                                                                .weight(1f)
//                                                                .height(200.dp)
//                                                                .clickable {
//                                                                    navController.navigate(
//                                                                        Screen.Plant.route.replace(
//                                                                            "{goalId}",
//                                                                            goalWithPlantPicture.goal.goalId.toString()
//                                                                        )
//                                                                },
//                                                            contentScale = ContentScale.Crop
//
//                                                        )
//                                                    }
//                                                    // Add empty boxes to fill the row if it's not complete
//                                                    if (rowImages == null) {
//                                                        Box(
//                                                            modifier = Modifier
//                                                                .size(130.dp)
//                                                                .padding(5.dp),
//                                                        )
//                                                    }
//                                                    )
//                                                },
//                                                contentScale = ContentScale.Crop
//                                                )
//
//                                            }
//                                                    // Fill remaining columns if less than 3 goals in the row
//                                                    repeat(3 - rowItems.size) {
//                                                Box(
//                                                    modifier = Modifier
//                                                        .weight(1f)
//                                                        .aspectRatio(1f)
//                                                        .background(Color.LightGray)
//                                                )
//                                            }
//                                                    Spacer(
//                                                    modifier = Modifier
//                                                        .fillMaxWidth()
//                                                        .padding(
//                                                            horizontal = 10.dp, vertical = 15.dp
//                                                        )
//                                                        .height(4.dp)
//                                                        .background(Color.DarkGray),
//                                        )
//                                    }
//                        }
//                    }
//
//
//                }
//            }
//        }


