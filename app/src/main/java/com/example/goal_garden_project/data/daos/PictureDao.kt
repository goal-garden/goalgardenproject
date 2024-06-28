package com.example.goal_garden_project.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.goal_garden_project.models.Picture
import kotlinx.coroutines.flow.Flow
@Dao
interface PictureDao {


    @Query("SELECT * FROM pictures WHERE plantId = :plantId")
    fun getPicturesForPlant(plantId: Long): Flow<List<Picture>>

    @Query("SELECT * FROM pictures WHERE pictureId = :pictureId")
    fun getPictureById(pictureId: Long): Flow<Picture?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPicture(picture: Picture): Long

    @Update
    suspend fun updatePicture(picture: Picture)

    @Delete
    suspend fun deletePicture(picture: Picture)

    @Query("SELECT * FROM pictures GROUP BY plantId HAVING progressionStage = MAX(progressionStage)")
    fun getAllLastImages(): Flow<List<Picture>>
}