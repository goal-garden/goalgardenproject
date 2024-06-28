package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.daos.PictureDao
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.Picture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class AddViewModel (private val repository: GoalRepository, private val repository2: PictureRepository
) : ViewModel() {

    private val _pictures = MutableStateFlow<List<Picture>?>(null)
    val pictures: StateFlow<List<Picture>?> = _pictures.asStateFlow()


    //hier muss ich dann nur die liste von verfügbaren plants laden

    init {
        viewModelScope.launch {
            repository2.getAllLastImages().distinctUntilChanged()
                .collect { pictures ->
                    _pictures.value = pictures
                    println("hey")
                    println(pictures)
                }
        }
    }
    fun addGoal(goal: Goal) {
        viewModelScope.launch {
            repository.addGoal(goal)
            repository
        }
    }

}
