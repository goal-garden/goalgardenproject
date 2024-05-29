package com.example.goal_garden_project.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Goals")
 class Goal(
    @PrimaryKey(autoGenerate = true)       //Assign the id a default value of 0, which is necessary for the id to auto generate id values.
    val dbId:Long =0,
    val goalId: String,
    val plantId: String,
    val currentProgressionImageNumber: Int, //so the right picture gets displayed
    val title: String,
    val description: String,
    //val date: Date, //maybe?      //save as milliseconds and convert it then to date
    val tasks: String, //later the TASK class
    var isFulfilled: Boolean = false,
     //later we could add parameter to change color of pot or form or whatsoever..
    ){
    override fun equals(other: Any?): Boolean {     //recomposition purpose of database
        return super.equals(other)
    }
}





