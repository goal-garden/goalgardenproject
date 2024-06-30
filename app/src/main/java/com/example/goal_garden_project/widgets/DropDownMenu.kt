package com.example.goal_garden_project.widgets

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.goal_garden_project.data.daos.GoalDao
import com.example.goal_garden_project.models.GoalWithPlantPicture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDropdownMenu(
    context: Context,
    goalsWithPlantPicture: List<GoalDao.IdImageTitle>?, //image, title , id (1 mal plant, 1 mal goal)
    plantName: String,
    onPlantSelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = plantName,
            onValueChange = { /* No-op */ },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable { expanded = true },
            readOnly = true,
            label = { Text("Plant") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            goalsWithPlantPicture?.forEach { plant ->
                DropdownMenuItem(
                    leadingIcon = {
                        Image(
                            painter = painterResource(
                                id = context.resources.getIdentifier(
                                    plant.image, "drawable", context.packageName
                                )
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(90.dp)
                        )
                    },
                    text = { Text(text = plant.title) },
                    onClick = {
                        onPlantSelected(plant.id.toString(), plant.title)
                        expanded = false
                    }
                )
            }
        }
    }
}
