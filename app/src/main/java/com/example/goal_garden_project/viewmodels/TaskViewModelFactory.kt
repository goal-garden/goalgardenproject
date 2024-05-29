package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class TaskViewModelFactory(//private val repository: MovieRepository
 ): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GoalViewModel::class.java)){
            return GoalViewModel() as T //repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}