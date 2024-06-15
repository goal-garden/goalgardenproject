package com.example.goal_garden_project.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.GoalWithTasks
import com.example.goal_garden_project.models.Picture
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    data class GoalImageTuple(
        val goalId: Long,
        val imageUrl: String
    )
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGoal(goal: Goal) //insert

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("SELECT * FROM goals")
    fun getAllGoals(): Flow<List<Goal>>

    @Query("SELECT plantId FROM goals")
    fun getAllGoalPlantID(): Flow<List<Goal>>

    @Transaction
    @Query("SELECT * FROM goals WHERE dbId = :goalId")
    fun getGoalWithTasksById(goalId: Long): Flow<GoalWithTasks>

    @Transaction
    @Query("SELECT * FROM goals WHERE goalId = :goalId")
    fun getGoalById(goalId: Long): Flow<Goal>

    @Query("""
        SELECT p.* FROM pictures p 
        INNER JOIN goals g ON p.plantId = g.plantId 
        WHERE g.dbId = :goalId AND p.progressionStage = g.imageProgressionNumber 
        ORDER BY p.progressionStage DESC LIMIT 1
    """)
    fun getCurrentPlantImage(goalId: Long): Flow<Picture?>

    @Query("""
        SELECT goalId, pictures.imageUrl
    FROM goals
    JOIN pictures ON goals.plantId = pictures.plantId
    AND pictures.progressionStage = goals.imageProgressionNumber
    LIMIT 1;
    """)
    fun getAllGoalImages(): Flow<List<GoalImageTuple>>



    @Transaction
    @Query("SELECT * FROM goals WHERE isFulfilled = 0")
    fun getUnfinishedGoalsWithTasks(): Flow<List<GoalWithTasks>>

    @Transaction
    @Query("SELECT * FROM goals WHERE isFulfilled = 1")
    fun getFinishedGoalsWithTasks(): Flow<List<GoalWithTasks>>
}
