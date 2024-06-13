package com.example.goal_garden_project.data

import SeedDatabaseWorker
import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.models.Goal
import com.example.goal_garden_project.models.Picture
import com.example.goal_garden_project.models.Plant
import com.example.goal_garden_project.models.Task
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.goal_garden_project.data.daos.PictureDao
import com.example.goal_garden_project.data.daos.PlantDao


@Database(
    entities = [Goal::class, Plant::class, Picture::class, Task::class], //thats how later more can be added: , MovieImage::class], // tables in the db       //brauch ich da nd auch noch movieWith images?
    version = 4, // schema version; whenever you change schema you have to increase the version number
    exportSchema = false // for schema version history updates
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao // Dao instance so that the DB knows about the Dao
    abstract fun plantDao(): PlantDao
    abstract fun pictureDao(): PictureDao
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
                        .addCallback(seedDatabaseCallback(context))
                        .build() // create an instance of the db
                        .also {
                            instance = it   // override the instance with newly created db
                        }

                }
        }

        private fun seedDatabaseCallback(context: Context): RoomDatabase.Callback {
            return object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val workRequest = OneTimeWorkRequest.Builder(SeedDatabaseWorker::class.java).build()
                    WorkManager.getInstance(context).enqueue(workRequest)
                }
            }
        }
    }
}


