package com.example.goal_garden_project.data.repositories
import com.example.goal_garden_project.data.daos.PictureDao
import com.example.goal_garden_project.models.Picture
import kotlinx.coroutines.flow.Flow

class PictureRepository(private val pictureDao: PictureDao) {
    suspend fun addPicture(picture: Picture) = pictureDao.addPicture(picture)

    suspend fun updatePicture(picture: Picture) = pictureDao.updatePicture(picture)

    suspend fun deletePicture(picture: Picture) = pictureDao.deletePicture(picture)

    fun getPicturesForPlant(plantId: Long): Flow<List<Picture>> = pictureDao.getPicturesForPlant(plantId)

    fun getPictureById(pictureId: Long): Flow<Picture?> = pictureDao.getPictureById(pictureId)


    fun getAllLastImages(): Flow<List<PictureDao.PicturePlant>> = pictureDao.getAllLastImages()

}