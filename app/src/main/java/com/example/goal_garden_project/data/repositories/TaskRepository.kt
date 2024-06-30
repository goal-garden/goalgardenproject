package com.example.goal_garden_project.data.repositories

import com.example.goal_garden_project.data.daos.TaskDao
import com.example.goal_garden_project.models.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun addTask(task: Task) = taskDao.addTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    fun getTasksForGoal(goalId: Long): Flow<List<Task>> = taskDao.getTasksForGoal(goalId)

    fun getTaskById(taskId: Long): Flow<Task?> = taskDao.getTaskById(taskId)

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
}