package com.example.asteroidradarapp.work


import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradarapp.Constants
import com.example.asteroidradarapp.api.getNextSevenDaysFormattedDates
import com.example.asteroidradarapp.repository.AsteroidRoomRebo
import com.example.asteroidradarapp.roomdatabase.AsteroidDataBase
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class AsteroidsWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val db = AsteroidDataBase.getInstance(applicationContext)
        val asteroidDao = db.asteroidDao()
        val repository = AsteroidRoomRebo(asteroidDao)
        val startDay = getNextSevenDaysFormattedDates()[0]
        val endDate = getNextSevenDaysFormattedDates()[7]

        //previous day :
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val previousDayTime = calendar.time
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val previousDayDate = dateFormat.format(previousDayTime)

        // here i catch the data that come using fun from repository :)
        repository.getAsteroidData(
            startDay,
            endDate, Constants.API_KEY
        )

        //To delete previous date :
        asteroidDao.deletePreviousDayAsteroids(previousDayDate)

        return try {
            Result.success()
        } catch (E: HttpException) {
            Result.retry()
        }
    }
}