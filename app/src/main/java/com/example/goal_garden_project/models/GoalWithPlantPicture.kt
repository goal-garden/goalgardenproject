package com.example.goal_garden_project.models

import androidx.room.Embedded
import androidx.room.Relation

class GoalWithPlantPicture(
    val title: String,
    val pictureId: Int,
    val plantId: Int,
    val progressionStage: Int,
    val imageUrl: String,
    val goalId: Int
)