package com.example.goal_garden_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.data.daos.PictureDao
import com.example.goal_garden_project.data.repositories.GoalRepository
import com.example.goal_garden_project.data.repositories.PictureRepository
import com.example.goal_garden_project.data.repositories.PlantRepository
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.Picture
import com.example.goal_garden_project.models.PlantWithPictures
import com.example.goal_garden_project.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class AddTaskViewModel (private val repository: GoalRepository, private val repository2: TaskRepository
) : ViewModel() {

    private val _goalsWithImageAndTitle = MutableStateFlow<List<GoalDao.IdImageTitle>?>(null)
    val goalsWithImageAndTitle: StateFlow<List<GoalDao.IdImageTitle>?> = _goalsWithImageAndTitle.asStateFlow()


    private val _goalId = MutableStateFlow<Long>(0)
    val goalId: StateFlow<Long> = _goalId.asStateFlow()


    init {
        viewModelScope.launch {
            repository.getAllGoalsWithImageAndTitle().distinctUntilChanged().collect{
                    goals ->
                _goalsWithImageAndTitle.value = goals
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository2.addTask(task)
        }
    }
    fun addTasks(goalId:Long, tasks:List<Task>){
        viewModelScope.launch {
            println(goalId)
            tasks.forEach { task ->
                repository2.addTask(task.copy(goalId = goalId))
            }
        }
    }
/*
    fun addGoalAndTasks(goal:Goal, tasks:List<Task>){
        viewModelScope.launch {
            var goalId = repository.addGoal(goal)
            println("hello"+ goalId)
            tasks.forEach { task->
                repository2.addTask(task.copy(goalId = goalId))
            }
        }

    }

 */
}
