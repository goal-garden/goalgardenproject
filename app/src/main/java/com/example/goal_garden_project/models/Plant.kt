package com.example.goal_garden_project.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "plants",
    /*
    foreignKeys = [
        ForeignKey(
            entity = Plant::class,
            parentColumns = ["plantId"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

     */
    //indices = [Index("plantId")]
)
data class Plant(
    @PrimaryKey(autoGenerate = true) val plantId: Long = 0,
    val name: String,
    val description: String? = null
)

//PlantID
//front image
//Plant Name - not necessary
//number of progression steps - not necessary (could also be sql questioned)



