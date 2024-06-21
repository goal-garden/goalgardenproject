package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.goal_garden_project.data.repositories.GoalRepository

class DetailViewModelFactory(private val repository: GoalRepository, private val goalId: Long
 ): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(repository = repository, goalId = goalId)
        as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}