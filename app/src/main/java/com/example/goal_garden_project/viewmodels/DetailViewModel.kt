package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.repositories.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

//use for plant screen and detail screen!!!!
class DetailViewModel (private val repository: GoalRepository, goalId:Long
) : ViewModel() {



    val hello = "nadine"



    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl.asStateFlow()

    init {

        viewModelScope.launch {
            repository.getCurrentPlantImage(goalId)
                .collect { imageUrl ->
                    if (imageUrl != null) {
                        println("Current plant image URL for goal $goalId: $imageUrl")
                    } else {
                        println("No image URL found for goal $goalId")
                    }
                    _imageUrl.value = imageUrl
                    println("Current image URL: $imageUrl")
                }
        }

        println("woooooist fehler")
        println(imageUrl)
    }




}
