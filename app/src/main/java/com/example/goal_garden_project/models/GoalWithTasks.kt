package com.example.goal_garden_project.models

import androidx.room.Embedded
import androidx.room.Relation

data class GoalWithTasks(
    @Embedded val goal: Goal,
    @Relation(
        parentColumn = "goalId",
        entityColumn = "goalId"
    )
    val tasks: List<Task>
)
