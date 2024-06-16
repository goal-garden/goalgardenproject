package com.example.goal_garden_project.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goal_garden_project.R
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.navigation.Screen
import com.example.goal_garden_project.viewmodels.DetailViewModel
import com.example.goal_garden_project.viewmodels.DetailViewModelFactory

import com.example.goal_garden_project.widgets.SimpleTopBar


@Composable
fun PlantScreen(goalId: Long, navController: NavController){//, moviesViewModel: HomeViewModel) {

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

    var showDialog by remember { mutableStateOf(false) }




    Scaffold(
        modifier= Modifier
            .fillMaxSize(),
        topBar = { SimpleTopBar(text = goalId.toString(), backButton = true, navController = navController) },

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
            Column (modifier = Modifier.fillMaxSize()){

                Image(
                    painter = painterResource(id = R.drawable.sonnenb1),
                    contentDescription = "my-goal",
                    modifier = Modifier
                        .size(300.dp)
                        .padding(5.dp),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(56.dp),
                    verticalAlignment = Alignment.Bottom,
                    //horizontalArrangement = Arrangement.SpaceEvenly

                ) {
                    Box (contentAlignment = Alignment.BottomStart){
                        Button(
                            onClick = {
                                // navigation  to another screen
                                navController.navigate(Screen.Detail.route.replace("{goalId}", goalId.toString()))
                            },
                            modifier = Modifier
                                .padding(20.dp)
                                .size(100.dp)
                                .border(3.dp, Color.Black, CircleShape)

                                .clip(CircleShape),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(Color.White, Color.Magenta),


                            ) {
                            Icon(imageVector = Screen.Detail.icon, contentDescription = "Navigate",  modifier = Modifier.size(36.dp))
                        }
                    }

                    Box (contentAlignment =
                                Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()){
                        Button(
                            onClick = {
                                showDialog = true
                            },
                            modifier = Modifier
                                .padding(20.dp)
                                .size(100.dp)
                                .border(3.dp, Color.Black, CircleShape)
                                .clip(CircleShape), // Clip the button to make it circular
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                Color.White,
                                Color.DarkGray
                            ),          //first color = background, second color = icon color
                            //shape = RoundedCornerShape(10) also looks good
                        ) {
                            Text(
                                text = "water",
                                textAlign = TextAlign.Center, // Center aligns the text horizontally
                                fontSize = 20.sp, // Increase font size
                                modifier = Modifier
                                    .align(Alignment.CenterVertically) // Center aligns vertically
                            )
                            //Icon(imageVector = Icons.Default.Send, contentDescription = "Open Popup", modifier = Modifier.size(36.dp))
                        }
                    }


                    // Dialog
                    if (showDialog) {
                        Dialog(
                            onDismissRequest = { showDialog = false }
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(color = Color.White)
                                    .size(300.dp)
                            ) {
                                Text(text = "This is a popup for watering the plant!")
                            }
                        }
                    }
                }
            }




        }
    }

}



