package com.example.goal_garden_project.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "goals",
    foreignKeys = [
        ForeignKey(
            entity = Plant::class,
            parentColumns = ["plantId"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],


    indices = [Index(value = ["goalId"]), Index(value = ["plantId"])]
)
data class Goal(
    @PrimaryKey(autoGenerate = true)       //Assign the id a default value of 0, which is necessary for the id to auto generate id values.
    var goalId: Long = 0,
    var plantId: Long,
    var progressionStage: Int = 0, //so the right picture gets displayed, initially 0
    var title: String,
    var description: String,
    var date: Int,      //save as milliseconds and convert it then to date
    var isFulfilled: Boolean = false,
    var reminderOn: Boolean = false,
    var reminderTime: Int =0,
    var reminderInterval: Int =0, //in days
    var isSeeded: Boolean = true
)
//later we could add parameter to change color of pot or form or whatsoever..
//    ){
//    override fun equals(other: Any?): Boolean {     //recomposition purpose of database
//        return super.equals(other)
//    }
//}





