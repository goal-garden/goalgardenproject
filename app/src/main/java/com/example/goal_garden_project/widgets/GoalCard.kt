package com.example.goal_garden_project.widgets

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.goal_garden_project.models.GoalWithTasks
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun GoalCard(
    goalWithTasks: GoalWithTasks,
    imageResourceId: Int,
    totalPicturesNumber: Int,
    onCapture: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val composeView = remember { ComposeView(context) }
    Log.d("total pic n", "$totalPicturesNumber")
    // Set content of ComposeView
    composeView.setContent {
        Card(
            modifier = Modifier.width(IntrinsicSize.Min),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = goalWithTasks.goal.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Image(
                    painter = painterResource(id = imageResourceId),
                    contentDescription = "Goal Image",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Description: ${goalWithTasks.goal.description}",
                    style = MaterialTheme.typography.bodyMedium
                )
                val date = formatTimestampAsString(goalWithTasks.goal.date)
                Text(
                    text = "Date created: $date",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Status: ${if (goalWithTasks.goal.isFulfilled) "Completed" else "In Progress"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                val progress =
                    (goalWithTasks.goal.progressionStage.toFloat() / totalPicturesNumber * 100).coerceAtMost(
                        100f
                    )
                Log.d("sssss", "${goalWithTasks.goal.progressionStage.toFloat()}")
                AchievementProgress(title = "", progress = progress)
            }
        }
    }

    // Create and return the bitmap
    LaunchedEffect(Unit) {
        val bitmap = Bitmap.createBitmap(
            composeView.width,
            composeView.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        composeView.draw(canvas)
        onCapture(bitmap)
    }

    // Add the ComposeView to the layout
    AndroidView(
        factory = { composeView },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}

@SuppressLint("SimpleDateFormat")
fun formatTimestampAsString(timestamp: Long): String {
    val date = Date(timestamp)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    return dateFormat.format(date)
}