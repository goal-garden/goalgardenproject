package com.example.goal_garden_project.data

import com.example.goal_garden_project.models.Goal
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val goalDao: GoalDao) {

        // Suspend indicates a long running async operation - coroutines
        suspend fun addGoal(goal: Goal) = goalDao.addGoal(goal)
        suspend fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)
        suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)

        // Flow indicates a long running async operation with multiple values
        fun getAllGoals(): Flow<List<Goal>> = goalDao.getAllGoals()
        fun getFinishedGoals(): Flow<List<Goal>> = goalDao.getFinishedGoals()
        fun getNotFinishedGoals(): Flow<List<Goal>> = goalDao.getFinishedGoals()
        fun getGoalById(id: String): Flow<Goal?> = goalDao.getGoalById(id)


}
