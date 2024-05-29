package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class DetailViewModelFactory(//private val repository: MovieRepository, private val movieId: String
 ): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel()//repository = repository, movieId = movieId)
        as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}