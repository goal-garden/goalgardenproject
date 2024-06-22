package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
//use for task screen
class TaskViewModel (//private val repository: MovieRepository
) : ViewModel() {

    //private val _movies = MutableStateFlow(listOf<MovieWithImages>())
    //val movies: StateFlow<List<MovieWithImages>> = _movies.asStateFlow()


    init {
        viewModelScope.launch {
            //repository.getFavoriteMovies().distinctUntilChanged()
            //    .collect { listOfMovies ->
            //        _movies.value = listOfMovies
            //    }
        }
    }
}
