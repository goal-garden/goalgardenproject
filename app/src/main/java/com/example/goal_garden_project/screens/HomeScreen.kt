package com.example.goal_garden_project.screens



import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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


@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("DiscouragedApi")
@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val goalRepository = GoalRepository(goalDao = db.goalDao())
    val factory = GoalViewModelFactory(repository = goalRepository)
    val viewModel: GoalViewModel = viewModel(factory = factory)


    val goalsWithPlantPicture by viewModel.goalsWithPlantPicture.collectAsState()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            SimpleBottomBar(navController, Screen.Add.route)
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
