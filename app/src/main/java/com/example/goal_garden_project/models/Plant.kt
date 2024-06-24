package com.example.goal_garden_project.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "plants",
    indices = [Index(value = ["plantId"])]
)
data class Plant(
    @PrimaryKey(autoGenerate = true) val plantId: Long = 0,
    val name: String,
    val description: String? = null
)

//PlantID
//front image




