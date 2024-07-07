package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.data.daos.PictureDao
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.Picture
import com.example.goal_garden_project.models.PlantWithPictures
import com.example.goal_garden_project.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class AddViewModel (private val repository: GoalRepository, private val repository2: PlantRepository
) : ViewModel() {

    private val _pictures = MutableStateFlow<List<GoalDao.IdImageTitle>?>(null)
    val pictures: StateFlow<List<GoalDao.IdImageTitle>?> = _pictures.asStateFlow()

    private var _goalId = MutableStateFlow<Long>(0)
    var goalId: StateFlow<Long> = _goalId.asStateFlow()

    private var goalId2:Long =0


    //hier muss ich dann nur die liste von verfügbaren plants laden

    init {
        viewModelScope.launch {
            repository2.getAllLastImages()
                .collect { pictures ->
                    _pictures.value = pictures
                    println("hey")
                    println(pictures)
                }
        }
    }
    fun addGoal(goal: Goal) {
        viewModelScope.launch {
            repository.addGoal(goal).collect{id ->_goalId.value}

            println("hello"+ goalId)
                    }
    }

    fun addGoal2(goal: Goal): Long{
        viewModelScope.launch {
            goalId2=repository.addGoal2(goal)
            println("hello new "+ goalId2)
        }
        return goalId2
    }

    fun insertGoal(goal: Goal): LiveData<Long> {
        val result = MutableLiveData<Long>()
        viewModelScope.launch {
            repository.addGoal(goal).collect { goalId ->
                result.postValue(goalId)
            }
        }
        return result
    }
    suspend fun addGoal3(goal: Goal): Long {
        var goalId: Long = -1
        repository.addGoal(goal).collect {
            goalId = it
        }
        return goalId
    }


}
