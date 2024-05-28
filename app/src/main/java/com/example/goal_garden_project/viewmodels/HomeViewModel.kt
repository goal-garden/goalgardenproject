package com.example.goal_garden_project.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Inherit from ViewModel class
class HomeViewModel(
        //private val repository: MovieRepository
) : ViewModel()  {

        //private val _movies = MutableStateFlow(listOf<MovieWithImages>())
        //val movies: StateFlow<List<MovieWithImages>> = _movies.asStateFlow()

        init {
                viewModelScope.launch {
                        //repository.getAllMovies().distinctUntilChanged()
                        //        .collect { listOfMovies ->
                        //                _movies.value = listOfMovies
                        //        }
                }
        }




}

