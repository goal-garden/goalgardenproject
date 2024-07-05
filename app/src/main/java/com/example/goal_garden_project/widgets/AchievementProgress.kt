package com.example.goal_garden_project.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.goal_garden_project.ui.theme.CustomGreen

@Composable
fun AchievementProgress(title: String, progress: Float) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progress / 100,
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .padding(start = 10.dp, end = 10.dp),
            trackColor = Color.LightGray,
            color = CustomGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${progress.toInt()}%",
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}