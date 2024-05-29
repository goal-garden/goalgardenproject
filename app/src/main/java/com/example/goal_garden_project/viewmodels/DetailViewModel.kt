package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//use for plant screen and detail screen!!!!
class DetailViewModel (//private val repository: MovieRepository, movieId:String
) : ViewModel() {

    //just a query by id



    //private val _movie = MutableStateFlow<MovieWithImages?>(null)
    //val movie: StateFlow<MovieWithImages?> = _movie.asStateFlow()


    init {
        viewModelScope.launch {
            //repository.getMovieById(movieId).distinctUntilChanged()
            //    .collect { movie ->
            //        _movie.value = movie
            //    }
        }
    }

}
