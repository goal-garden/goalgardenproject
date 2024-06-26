package com.example.goal_garden_project.screens


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.viewmodels.GoalViewModel
import com.example.goal_garden_project.viewmodels.GoalViewModelFactory
import com.example.goal_garden_project.widgets.SimpleBottomBar


@Composable
fun ListScreen(navController: NavController) {

    val db = AppDatabase.getDatabase(LocalContext.current)
    val repository = GoalRepository(goalDao = db.goalDao())
    val factory = GoalViewModelFactory(repository = repository)
    val viewModel: GoalViewModel = viewModel(factory = factory)

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filterType by remember { mutableStateOf(GoalFilterType.ALL) }

    val allGoals by viewModel.goals.collectAsState()
    val inProgressGoals by viewModel.unfinishedGoals.collectAsState()
    val completedGoals by viewModel.finishedGoals.collectAsState()

    val items = when (filterType) {
        GoalFilterType.ALL -> allGoals
        GoalFilterType.IN_PROGRESS -> inProgressGoals
        GoalFilterType.COMPLETED -> completedGoals
    }

    Column {
        SearchBar(searchQuery) { query ->
            searchQuery = query
        }
        FilterButtons(filterType) { selectedFilter ->
            filterType = selectedFilter
        }
        ItemList(items, searchQuery.text)
    }
}

@Composable
fun SearchBar(searchQuery: TextFieldValue, onQueryChanged: (TextFieldValue) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search") },
        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
    )
}

@Composable
fun FilterButtons(currentFilter: GoalFilterType, onFilterChanged: (GoalFilterType) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { onFilterChanged(GoalFilterType.ALL) }) {
            Text(text = "All Goals")
        }
        Button(onClick = { onFilterChanged(GoalFilterType.IN_PROGRESS) }) {
            Text(text = "In Progress")
        }
        Button(onClick = { onFilterChanged(GoalFilterType.COMPLETED) }) {
            Text(text = "Completed")
        }
    }
}

@Composable
fun ItemList(items: List<Goal>, searchQuery: String) {
    val filteredItems = items.filter { it.title.contains(searchQuery, ignoreCase = true) }

    LazyColumn {
        items(filteredItems) { item ->
            Text(
                text = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 18.sp
            )
        }
    }
}

enum class GoalFilterType {
    ALL, IN_PROGRESS, COMPLETED
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
   ListScreen(navController = rememberNavController())
}



