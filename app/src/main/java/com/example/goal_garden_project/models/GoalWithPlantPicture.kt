package com.example.goal_garden_project.models

import androidx.room.Embedded
import androidx.room.Relation

class GoalWithPlantPicture(
    @Embedded
    val goal: Goal,
    @Relation(
        parentColumn = "plantId",
        entityColumn = "plantId",
    )
    val picture: Picture?
)