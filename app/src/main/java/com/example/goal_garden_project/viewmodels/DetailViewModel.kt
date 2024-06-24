package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.models.GoalWithPlantPicture
import com.example.goal_garden_project.models.GoalWithTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

//use for plant screen and detail screen!!!!
class DetailViewModel(
    private val repository: GoalRepository
) : ViewModel() {

    //    private val _imageUrl = MutableStateFlow<String?>(null)
//    val imageUrl: StateFlow<String?> = _imageUrl.asStateFlow()
    private val _specificGoal = MutableStateFlow<GoalWithTasks?>(value = null)
    val specificGoal: StateFlow<GoalWithTasks?> = _specificGoal.asStateFlow()

    private val _goalWithPlantPicture = MutableStateFlow<GoalWithPlantPicture?>(value = null)
    val goalWithPlantPicture: StateFlow<GoalWithPlantPicture?> = _goalWithPlantPicture.asStateFlow()


    fun getGoalById(id: Long): StateFlow<GoalWithTasks?> {
        viewModelScope.launch {
//            movieRepository.getMovieById(movieId = movieId).collect { movie ->
//                _specificMovie.value = movie
            repository.getGoalWithTasksById(id = id).collect() { goal ->
                _specificGoal.value = goal
            }
        }
        return _specificGoal.asStateFlow()
    }

    fun getGoalByIdWithPicture(id: Long): StateFlow<GoalWithPlantPicture?> {
        viewModelScope.launch {
            repository.getGoalByIdWithPlantPicture(id)
                .collect { goalWithCurrentPlant ->
                    _goalWithPlantPicture.value = goalWithCurrentPlant
                }
        }
        return _goalWithPlantPicture.asStateFlow()
    }


//    init {
//        viewModelScope.launch {
//            repository.getCurrentPlantImage()
//                .collect { imageUrl ->
//                    if (imageUrl != null) {
//                        println("Current plant image URL for goal $goalId: $imageUrl")
//                    } else {
//                        println("No image URL found for goal $goalId")
//                    }
//                    _imageUrl.value = imageUrl
//                    println("Current image URL: $imageUrl")
//                }
//        }
//        println("woooooist fehler")
//        println(imageUrl)
//    }
}
