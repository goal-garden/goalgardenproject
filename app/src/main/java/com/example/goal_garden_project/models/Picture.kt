package com.example.goal_garden_project.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pictures",

    foreignKeys = [ForeignKey(
        entity = Plant::class,
        parentColumns = ["plantId"],
        childColumns = ["plantId"],
        onDelete = ForeignKey.CASCADE
    )],


    indices = [Index(value = ["pictureId"]), Index(value = ["plantId"])]
)
data class Picture(
    //PictureID
    //url -unique
    //plantID
    //progress number
    @PrimaryKey(autoGenerate = true)
    var pictureId: Long = 0,
    var plantId: Long,
    var imageUrl: String,
    var progressionStage: Int,
)

