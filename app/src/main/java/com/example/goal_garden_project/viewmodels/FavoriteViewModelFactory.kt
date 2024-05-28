package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class FavoriteViewModelFactory(//private val repository: MovieRepository
 ): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel() as T //repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}