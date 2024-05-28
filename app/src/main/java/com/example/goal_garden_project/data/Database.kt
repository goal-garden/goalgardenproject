package com.example.goal_garden_project.viewmodels

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.goal_garden_project.models.Goal

@Database(
    entities = [Goal::class], //thats how later more can be added: , MovieImage::class], // tables in the db       //brauch ich da nd auch noch movieWith images?
    version = 1, // schema version; whenever you change schema you have to increase the version number
    exportSchema = false // for schema version history updates
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDao(): Dao // Dao instance so that the DB knows about the Dao
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


