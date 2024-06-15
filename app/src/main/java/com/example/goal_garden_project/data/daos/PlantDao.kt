package com.example.goal_garden_project.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.goal_garden_project.models.Plant
import com.example.goal_garden_project.models.PlantWithPictures
import kotlinx.coroutines.flow.Flow
@Dao
interface PlantDao {
    @Transaction
    @Query("SELECT * FROM plants")
    fun getAllPlants(): Flow<List<Plant>>

    @Transaction
    @Query("SELECT * FROM plants WHERE plantId = :plantId")
    fun getPlantById(plantId: Long): Flow<Plant>

    @Transaction
    @Query("SELECT * FROM plants WHERE plantId = :plantId")
    fun getPlantWithPicturesById(plantId: Long): Flow<PlantWithPictures>

    @Transaction
    @Query("SELECT * FROM plants")
    fun getAllPlantsWithPictures(): Flow<List<PlantWithPictures>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlant(plant: Plant): Long

    @Update
    suspend fun updatePlant(plant: Plant)

    @Delete
    suspend fun deletePlant(plant: Plant)
}