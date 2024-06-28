package com.example.goal_garden_project.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.goal_garden_project.models.Picture
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
    @Query("SELECT * FROM plants")      //you dont need to join there because it automaticly fills in the pictures
    fun getAllPlantsWithPictures(): Flow<List<PlantWithPictures>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlant(plant: Plant): Long

    @Update
    suspend fun updatePlant(plant: Plant)

    @Delete
    suspend fun deletePlant(plant: Plant)
/*

    @Query("SELECT * FROM plants INNER JOIN pictures ON plants.plantId= pictures.plantId " +
            "" +
            " GROUP BY pictures.plantId HAVING progressionStage = MAX(progressionStage)")

    fun getAllLastImages(): Flow<List<PlantWithPictures>>

    @Query("SELECT plants.plantId, plants.description as plant.description, plants.name, pictures.pictureId FROM pictures JOIN plants GROUP BY plantId HAVING progressionStage = MAX(progressionStage)")
    fun getAllLastImages(): Flow<List<PlantWithPictures>>
    */      //i wanted to solve it like this but it wouldnt work

    @Query("SELECT *    FROM plants p INNER JOIN pictures pic ON p.plantId = pic.plantId"
    ) fun getAllLastImages(): Flow<List<PlantWithPictures>>
}