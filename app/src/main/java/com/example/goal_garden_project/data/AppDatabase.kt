package com.example.goal_garden_project.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.Picture
import com.example.goal_garden_project.models.Plant
import com.example.goal_garden_project.navigation.Screen

@Database(
    entities = [Goal::class, Plant::class, Picture::class, Screen.Task::class], //thats how later more can be added: , MovieImage::class], // tables in the db       //brauch ich da nd auch noch movieWith images?
    version = 2, // schema version; whenever you change schema you have to increase the version number
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
                     ],
    exportSchema = true // for schema version history updates
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao // Dao instance so that the DB knows about the Dao
    // add more daos here if you have multiple tables

    // declare as singleton - companion objects are like static variables in Java
    companion object {
        @Volatile   // never cache the value of instance
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance
                ?: synchronized(this) { // wrap in synchronized block to prevent race conditions
                    Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
                        .fallbackToDestructiveMigration() // if schema changes wipe the whole db - there are better migration strategies for production usage
                        .build() // create an instance of the db
                        .also {
                            instance = it   // override the instance with newly created db
                        }
                }
        }
    }
}


