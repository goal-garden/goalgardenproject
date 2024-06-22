package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.models.Goal
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

    private val _goalImages = MutableStateFlow(listOf<Picture>())
    val goalImages: StateFlow<List<Picture>> = _goalImages.asStateFlow()

    //contain grouping after finished/not finished/not seeded
    private val _unfinishedGoals = MutableStateFlow((listOf<Goal>()))
    val unfinishedGoals: StateFlow<List<Goal>> = _goals.asStateFlow()

    private val _finishedGoals = MutableStateFlow((listOf<Goal>()))
    val finishedGoals: StateFlow<List<Goal>> = _goals.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllGoals().distinctUntilChanged().collect { goals ->
                _goals.value = goals
            }
        }
//        viewModelScope.launch {
//            repository.getAllGoalImages().distinctUntilChanged().collect { images ->
//                _goalImages.value = images
//            }
//        }
    }

}
