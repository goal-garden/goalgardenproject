package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.models.Goal
import kotlinx.coroutines.launch

class AddViewModel (private val repository: GoalRepository
) : ViewModel() {

    //private val _plant = MutableStateFlow<MovieWithImages?>(null)
    //val movie: StateFlow<MovieWithImages?> = _movie.asStateFlow()


    //hier muss ich dann nur die liste von verfügbaren plants laden

    init {
        viewModelScope.launch {
            //repository.getMovieById(movieId).distinctUntilChanged()
            //    .collect { movie ->
            //        _movie.value = movie
            //    }
        }
    }
    fun addGoal(goal: Goal) {
        viewModelScope.launch {
            repository.addGoal(goal)
        }
    }

}
