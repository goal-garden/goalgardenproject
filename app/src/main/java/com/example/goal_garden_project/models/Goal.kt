package com.example.goal_garden_project.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Goals",
    indices = [Index(value = ["goalId"], unique = true)]
)
data class Goal(
    @PrimaryKey(autoGenerate = true)       //Assign the id a default value of 0, which is necessary for the id to auto generate id values.
    var dbId: Long = 0,
    var goalId: Long,
    var plantId: Long,
    var imageProgressionNumber: Int, //so the right picture gets displayed
    var title: String,
    var description: String,
    var date: Int,      //save as milliseconds and convert it then to date
    var isFulfilled: Boolean = false
)
//later we could add parameter to change color of pot or form or whatsoever..
//    ){
//    override fun equals(other: Any?): Boolean {     //recomposition purpose of database
//        return super.equals(other)
//    }
//}





