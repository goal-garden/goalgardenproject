package com.example.goal_garden_project.models

import androidx.room.Embedded
import androidx.room.Relation

data class PlantWithPictures(
    @Embedded val plant: Plant,
    @Relation(
        parentColumn = "plantId",
        entityColumn = "plantId"
    )
    val pictures: List<Picture>
)

fun getPlants(): List<PlantWithPictures> {
    return listOf(
        PlantWithPictures(
            plant = Plant(
                1, "sunflower"
            ), pictures = listOf(
                Picture(1, 1, "sonnenb1", 0),
                Picture(2, 1, "sonnenb2", 1),
                Picture(3, 1, "sonnenb3", 2),
                Picture(4, 1, "sonnenb4", 3),
                Picture(5, 1, "sonnenb5", 4),
            )
        ),
        PlantWithPictures(
            plant = Plant(
                2, "strawberry"
            ), pictures = listOf(
                Picture(6, 2, "erd1", 0),
                Picture(7, 2, "erd2", 1),

            )
        ),


    )



    //add all plants available here
}
