package com.example.goal_garden_project.models

import androidx.room.Embedded
import androidx.room.Relation

class GoalWithPlantPicture(
    val title: String,
    val pictureId: Long,
    val plantId: Long,
    val progressionStage: Int,
    val imageUrl: String,
    val goalId: Int
)