package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
abstract class MovieListViewModel : ViewModel(){
        abstract fun toggleFavoriteMovie(movieId: String)
}