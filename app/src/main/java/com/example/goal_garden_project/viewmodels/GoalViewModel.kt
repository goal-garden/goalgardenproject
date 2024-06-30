package com.example.goal_garden_project.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.GoalWithPlantPicture
import com.example.goal_garden_project.models.GoalWithTasks
import com.example.goal_garden_project.models.Picture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

import kotlinx.coroutines.launch

//use for home and goal screen !!!!
class GoalViewModel(
    private val repository: GoalRepository
) : ViewModel() {

    private val _goals = MutableStateFlow((listOf<Goal>()))
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    private val _goalsWithPlantPicture = MutableStateFlow<List<GoalWithPlantPicture>>(emptyList())
    val goalsWithPlantPicture: StateFlow<List<GoalWithPlantPicture>> =
        _goalsWithPlantPicture.asStateFlow()

    private val _goalsWithImageAndTitle = MutableStateFlow<List<GoalDao.IdImageTitle>?>(null)
    val goalsWithImageAndTitle: StateFlow<List<GoalDao.IdImageTitle>?> = _goalsWithImageAndTitle.asStateFlow()

    //contain grouping after finished/not finished/not seeded
    private val _unfinishedGoals = MutableStateFlow((listOf<Goal>()))
    val unfinishedGoals: StateFlow<List<Goal>> = _goals.asStateFlow()

    private val _finishedGoals = MutableStateFlow((listOf<Goal>()))
    val finishedGoals: StateFlow<List<Goal>> = _goals.asStateFlow()

    private val _waitingToBeSeededGoals = MutableStateFlow((listOf<Goal>()))
    val waitingToBeSeededGoals: StateFlow<List<Goal>> = _waitingToBeSeededGoals.asStateFlow()

    init {

        viewModelScope.launch {
            repository.getAllGoals().distinctUntilChanged().collect { goals ->
                _goals.value = goals
                Log.d("GoalViewModel", "All goals: $goals")
            }
        }
        viewModelScope.launch {
            repository.getAllGoalsWithPlantPictures().distinctUntilChanged()
                .collect { goals ->
                    _goalsWithPlantPicture.value = goals
                }
        }
        viewModelScope.launch {
            repository.getUnfinishedGoals().distinctUntilChanged().collect { goals ->
                _unfinishedGoals.value = goals
                Log.d("GoalViewModel", "Unfinished goals: $goals")
            }
        }
        viewModelScope.launch {
            repository.getFinishedGoals().distinctUntilChanged().collect { goals ->
                _finishedGoals.value = goals
                Log.d("GoalViewModel", "Finished goals: $goals")
            }
        }
        viewModelScope.launch {
            repository.getAllGoalsWithImageAndTitle().distinctUntilChanged().collect{
                goals ->
                    _goalsWithImageAndTitle.value = goals

            }

        }
//        viewModelScope.launch {
//            repository.getUnseededGoals().distinctUntilChanged()
//                .collect { goals ->
//                    _waitingToBeSeededGoals.value = goals
//                }
//        }
    }

}
