package com.example.goal_garden_project.data.repositories

import com.example.goal_garden_project.data.daos.PlantDao
import com.example.goal_garden_project.models.Plant
import com.example.goal_garden_project.models.PlantWithPictures
import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao) {
    //we need extra dao and repository for it because the plants exists also without any goal for it
    //because when you first add a goal this possibilities will get displayed
    suspend fun addPlant(plant: Plant) = plantDao.addPlant(plant)

    suspend fun updatePlant(plant: Plant) = plantDao.updatePlant(plant)

    suspend fun deletePlant(plant: Plant) = plantDao.deletePlant(plant)

    fun getAllPlants(): Flow<List<Plant>> = plantDao.getAllPlants()

    fun getPlantById(plantId: Long): Flow<Plant?> = plantDao.getPlantById(plantId)

    fun getAllPlantsWithPictures(): Flow<List<PlantWithPictures>> = plantDao.getAllPlantsWithPictures()
    fun getPlantWithPictures(plantId: Long): Flow<PlantWithPictures?> = plantDao.getPlantWithPicturesById(plantId)
    fun getAllLastImages(): Flow<List<PlantWithPictures>> =plantDao.getAllLastImages()

}