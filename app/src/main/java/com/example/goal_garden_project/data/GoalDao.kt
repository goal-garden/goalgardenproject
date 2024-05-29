package com.example.goal_garden_project.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.goal_garden_project.models.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert
    suspend fun addGoal(goal: Goal) //insert
    @Update
    suspend fun updateGoal(goal: Goal)
    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Transaction
    @Query("SELECT * FROM Goals")
    fun getAllGoals(): Flow<List<Goal>>

    @Transaction
    @Query("SELECT * FROM Goals WHERE goalId LIKE :id ")
    fun getGoalById(id: String): Flow<Goal>

    @Transaction
    @Query("SELECT * FROM Goals WHERE isFulfilled = 0")
    fun getNotFinishedGoals(): Flow<List<Goal>>
    @Transaction
    @Query("SELECT * FROM Goals WHERE isFulfilled = 1")
    fun getFinishedGoals(): Flow<List<Goal>>
}
