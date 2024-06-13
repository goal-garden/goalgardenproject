package com.example.goal_garden_project.data.daos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.goal_garden_project.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskDao {
    @Query("SELECT * FROM task WHERE goalId = :goalId")
    fun getTasksForGoal(goalId: Long): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE taskId = :taskId")
    fun getTaskById(taskId: Long): Flow<Task?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}