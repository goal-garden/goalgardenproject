package com.example.goal_garden_project.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal_garden_project.data.repositories.TaskRepository
import com.example.goal_garden_project.models.GoalWithPlantPicture
import com.example.goal_garden_project.models.GoalWithTasks
import com.example.goal_garden_project.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

import kotlinx.coroutines.launch
//use for task screen
class TaskViewModel (private val repository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow(listOf<Task>())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _task = MutableStateFlow<Task?>(value = null)
    val task: StateFlow<Task?> = _task.asStateFlow()

    private val _unfinishedTasks = MutableStateFlow(listOf<Task>())
    val unfinishedTasks: StateFlow<List<Task>> = _unfinishedTasks.asStateFlow()



    init {

        viewModelScope.launch {
            repository.getAllTasks().distinctUntilChanged()
                .collect { tasks ->
                    _tasks.value = tasks
                }

        }
    }

    fun getUnfinishedTasks(goalId:Long): StateFlow<List<Task>>{
        viewModelScope.launch {
            repository.getUnfinishedTasks(goalId)
                .collect { tasks ->
                    _unfinishedTasks.value = tasks
                }
        }
        return _unfinishedTasks.asStateFlow()
    }

    fun markTaskAsFulfilled(taskId: Long) {
        viewModelScope.launch {
            repository.getTaskById(taskId).collect { task ->
                if (task != null) {
                    val updatedTask = task.copy(isFulfilled = true)
                    repository.updateTask(updatedTask)
                    // Update the _task state flow to reflect the change
                    _task.value = updatedTask
                }
            }
        }}

}
