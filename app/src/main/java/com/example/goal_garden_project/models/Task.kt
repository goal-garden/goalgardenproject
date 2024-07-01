package com.example.goal_garden_project.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = Goal::class,
        parentColumns = ["goalId"],
        childColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["goalId"]), Index(value = ["taskId"])]
)
data class Task(
    //taskID
    //task name
    //task description
    //date
    //done/not done
    //goalID
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0,
    var goalId: Long,
    var name: String,
    var description: String,
    var date: Int,
    var isFulfilled: Boolean,
    //var value: Long =1      //to know how much it contributes to a goal, to also determine when the plant should grow
)