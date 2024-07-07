package com.example.goal_garden_project.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.GoalWithPlantPicture
import com.example.goal_garden_project.models.Task

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


    //contain grouping after finished/not finished/not seeded
    private val _unfinishedGoals = MutableStateFlow((listOf<Goal>()))
    val unfinishedGoals: StateFlow<List<Goal>> = _unfinishedGoals.asStateFlow()

    private val _finishedGoals = MutableStateFlow((listOf<Goal>()))
    val finishedGoals: StateFlow<List<Goal>> = _finishedGoals.asStateFlow()

    private val _unseededGoals = MutableStateFlow((listOf<Goal>()))
    val unseededGoals: StateFlow<List<Goal>> = _unseededGoals.asStateFlow()

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
    }

    fun fetchUnfinishedGoals(): StateFlow<List<Goal>> {
        viewModelScope.launch {
            repository.getUnfinishedGoals().distinctUntilChanged().collect { goals ->
                _unfinishedGoals.value = goals
                Log.d("GoalViewModel", "Unfinished goals: $goals")
            }
        }
        return _unfinishedGoals.asStateFlow()
    }

    fun fetchFinishedGoals(): StateFlow<List<Goal>> {
        viewModelScope.launch {
            repository.getFinishedGoals().distinctUntilChanged().collect { goals ->
                _finishedGoals.value = goals
                Log.d("GoalViewModel", "Finished goals: $goals")
            }
        }
        return _finishedGoals.asStateFlow()
    }

    fun fetchUnseededGoals(): StateFlow<List<Goal>> {
        viewModelScope.launch {
            repository.getUnseededGoals().distinctUntilChanged().collect { goals ->
                _unseededGoals.value = goals
                Log.d("GoalViewModel", "Unseeded goals: $goals")
            }
        }
        return _unseededGoals.asStateFlow()
    }

    fun seedGoal(goalId: Long) {
        viewModelScope.launch {
            repository.getGoalById(goalId).collect { goal ->
                goal?.let {
                    val updatedGoal = it.copy(isSeeded = true) // Create a copy with isSeeded = true
                    repository.updateGoal(updatedGoal) // Update the goal in the repository
                }
            }
        }
    }
}
