package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.data.repositories.TaskRepository

class AddTaskViewModelFactory(private val repository: GoalRepository, private val repository2: TaskRepository
 ): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddTaskViewModel::class.java)){
            return AddTaskViewModel(repository = repository, repository2=repository2)
        as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}