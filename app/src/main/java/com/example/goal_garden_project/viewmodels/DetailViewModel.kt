package com.example.goal_garden_project.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.GoalWithPlantPicture
import com.example.goal_garden_project.models.GoalWithTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: GoalRepository
) : ViewModel() {

    private val _specificGoal = MutableStateFlow<GoalWithTasks?>(value = null)
    val specificGoal: StateFlow<GoalWithTasks?> = _specificGoal.asStateFlow()

    private val _goalWithPlantPicture = MutableStateFlow<GoalWithPlantPicture?>(value = null)
    val goalWithPlantPicture: StateFlow<GoalWithPlantPicture?> = _goalWithPlantPicture.asStateFlow()


    fun getGoalById(id: Long): StateFlow<GoalWithTasks?> {
        viewModelScope.launch {
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

    fun updateGoal(goalId: Long, title: String, description: String, date: Long, isFulfilled: Boolean) {
        viewModelScope.launch {
            val currentGoal = repository.getGoalById(goalId)
            currentGoal.collect { goal ->
                goal?.let {
                    val updatedGoal = Goal(
                        goalId = it.goalId,
                        plantId = it.plantId,
                        progressionStage = it.progressionStage,
                        title = title,
                        description = description,
                        date = date,
                        isFulfilled = isFulfilled
                    )
                    repository.updateGoal(updatedGoal)
                }
            }
        }
    }
    fun deleteGoal(goalId: Long) {
        viewModelScope.launch {
            repository.deleteGoalById(goalId)
        }
    }

}
