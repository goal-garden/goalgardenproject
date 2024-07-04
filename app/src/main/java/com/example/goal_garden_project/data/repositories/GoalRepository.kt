package com.example.goal_garden_project.data.repositories

import android.util.Log
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.GoalWithPlantPicture
import com.example.goal_garden_project.models.GoalWithTasks
import com.example.goal_garden_project.models.Picture
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val goalDao: GoalDao) {

    // Suspend indicates a long running async operation - coroutines
    suspend fun addGoal(goal: Goal): Long {
        val goalId = goalDao.addGoal(goal)
        return goalId
    }
    suspend fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)
    suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)
    suspend fun deleteGoalById(goalId: Long) = goalDao.deleteGoal(goalId)

    // Flow indicates a long running async operation with multiple values
    fun getAllGoals(): Flow<List<Goal>> = goalDao.getAllGoals()

    //fun getAllGoalPlantID(): Flow<List<Goal>> = goalDao.getAllGoalPlantID()
    fun getAllGoalsWithPlantPictures(): Flow<List<GoalWithPlantPicture>> =
        goalDao.getGoalsWithPlantPictures()

    fun getGoalByIdWithPlantPicture(id: Long): Flow<GoalWithPlantPicture?> =
        goalDao.getGoalByIdWithPlantPicture(id)

    //fun getAllGoalImages():Flow<List<GoalDao.GoalImageTuple>> = goalDao.getAllGoalImages()
    fun getCurrentPlantImage(id: Long): Flow<String?> = goalDao.getCurrentPlantImageUrl(id)
    fun getGoalWithTasksById(id: Long): Flow<GoalWithTasks?> = goalDao.getGoalWithTasksById(id)
    fun getGoalById(id: Long): Flow<Goal?> = goalDao.getGoalById(id)
    fun getFinishedGoals(): Flow<List<Goal>> = goalDao.getFinishedGoals()
    fun getUnfinishedGoals(): Flow<List<Goal>> = goalDao.getUnfinishedGoals()


    fun getAllGoalsWithImageAndTitle(): Flow<List<GoalDao.IdImageTitle>> = goalDao.getAllGoalsWithImageAndTitle()

}
