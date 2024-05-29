package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.goal_garden_project.data.GoalRepository

class AddViewModelFactory(private val repository: GoalRepository
 ): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddViewModel::class.java)){
            return AddViewModel(repository = repository)
        as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}