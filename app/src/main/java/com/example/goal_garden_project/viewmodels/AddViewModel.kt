package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.models.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class AddViewModel (private val repository: GoalRepository, private val repository2: PlantRepository
) : ViewModel() {

    private val _pictures = MutableStateFlow<List<GoalDao.IdImageTitle>?>(null)
    val pictures: StateFlow<List<GoalDao.IdImageTitle>?> = _pictures.asStateFlow()

    private var _goalId = MutableStateFlow<Long>(0)
    var goalId: StateFlow<Long> = _goalId.asStateFlow()

    init {      //for deciding between the possible plants
        viewModelScope.launch {
            repository2.getAllLastImages()
                .collect { pictures ->
                    _pictures.value = pictures
                }
        }
    }
    suspend fun addGoal(goal: Goal): Long {        //add goal
        var goalId: Long = -1
        repository.addGoal(goal).collect {
            goalId = it
        }
        return goalId
    }
    suspend fun getGoalImageById(id: Long): String{         //would be better to use this in addgoal screen for the notification handler but it doesnt work
        var goalUrl:String = ""
        repository.getCurrentPlantImage(id).collect {
            goalUrl = it
        }
        return goalUrl
    }
}
