import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.goal_garden_project.data.AppDatabase
import com.example.goal_garden_project.models.Picture
import com.example.goal_garden_project.models.getPlants
import kotlinx.coroutines.flow.forEach


class SeedDatabaseWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            val plantDao = AppDatabase.getDatabase(applicationContext).plantDao()
            val pictureDao  = AppDatabase.getDatabase(applicationContext).pictureDao()
            getPlants().forEach { plant ->
                plantDao.addPlant(plant.plant)
                plant.pictures.forEach{     //maybe make iteration later in dao
                    pictureDao.addPicture(it)
                }
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}