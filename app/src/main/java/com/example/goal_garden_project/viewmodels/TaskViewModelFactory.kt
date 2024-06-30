package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.TaskRepository

class TaskViewModelFactory(private val repository: TaskRepository, private val repository2: GoalRepository
 ): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskViewModel::class.java)){
            return TaskViewModel(repository = repository, repository2=repository2) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}