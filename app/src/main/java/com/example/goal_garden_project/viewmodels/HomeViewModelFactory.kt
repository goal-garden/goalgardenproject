package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//use the Factory pattern and tell viewModels() how to instantiate our MoviesViewModel
class HomeViewModelFactory(//private val repository: MovieRepository
 ): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel() as T //repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}