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

    fun updateGoal(goalId: Long, title: String, description: String, date: Long, isFulfilled: Boolean, isReminderSet: Boolean, reminderTime:Long, reminderInterval:Long) {
        viewModelScope.launch {
            val currentGoal = repository.getGoalById(goalId)
            currentGoal.collect { goal ->       //doesnt work??
                //this is for automatically setting to the last image when is fulfilled is marked
                var progressionNumber:Int=goal?.progressionStage?:0
                if (goal?.isFulfilled!=isFulfilled){    //if is a change
                    if(isFulfilled){
                        println("is now manually fulfilled and wans't before")
                        goal?.let {
                            repository.getMaxProgressionNumber(it.plantId).collect { maxProgression ->
                                progressionNumber = maxProgression
                                println(progressionNumber)      //this works still
                            }
                        }
                    }
                    else{
                        progressionNumber=0      //set it to the beginning when the plant was automatically marked as done but isn't actually, the pictures will start from zero again
                    }
                }


                goal?.let {
                    val updatedGoal = Goal(
                        goalId = it.goalId,
                        plantId = it.plantId,
                        progressionStage = progressionNumber, //it.progressionStage,//
                        title = title,
                        description = description,
                        date = date,
                        isFulfilled = isFulfilled,
                        reminderOn = isReminderSet,
                        reminderTime = reminderTime,
                        reminderInterval=reminderInterval
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
