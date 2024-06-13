package com.example.goal_garden_project.models

import androidx.room.Embedded
import androidx.room.Relation

data class PlantWithPictures(
    @Embedded val plant: Plant,
    @Relation(
        parentColumn = "plantId",
        entityColumn = "plantId"
    )
    val pictures: List<Picture>
)
