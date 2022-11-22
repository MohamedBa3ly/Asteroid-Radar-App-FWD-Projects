package com.example.asteroidradarapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.asteroidradarapp.Asteroid
import com.example.asteroidradarapp.PictureOfDay
import com.example.asteroidradarapp.State
import com.example.asteroidradarapp.api.AsteroidObjectApi
import com.example.asteroidradarapp.api.PictureOfDayObjectApi
import com.example.asteroidradarapp.api.getNextSevenDaysFormattedDates
import com.example.asteroidradarapp.api.parseAsteroidsJsonResult
import com.example.asteroidradarapp.roomdatabase.AsteroidDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRoomRebo(private val asteroidDao: AsteroidDao) {

    val readAllAsteroidsDetails: LiveData<List<Asteroid>> = asteroidDao.readAllAsteroidDetails()

    //suspend fun To add asteroid and use it in view model scope in (Main view model):
    suspend fun addAsteroidDetails(asteroid: List<Asteroid>) {
        asteroidDao.addAsteroidDetails(asteroid)
    }

    //suspend fun To update asteroid and use it in view model scope in (Main view model):
    suspend fun updateAsteroidDetails(asteroid: Asteroid) {
        asteroidDao.updateAsteroidDetails(asteroid)
    }

    //suspend fun To delete asteroid and use it in view model scope in (Main view model):
    suspend fun deleteAsteroidDetails(asteroid: Asteroid) {
        asteroidDao.deleteAsteroidDetails(asteroid)
    }

    //suspend fun To delete all asteroid and use it in view model scope in (Main view model):
    suspend fun deleteAllAsteroidDetails() {
        asteroidDao.deleteAllAsteroidDetails()
    }

    //suspend fun to get image and state when downloading in my app using flow :
    suspend fun getImageOfDays(apiKey: String): Flow<State<PictureOfDay?>> {
        return flow {
            emit(State.Loading)
            val result = PictureOfDayObjectApi.retrofitPictureService.getImageOfDay(apiKey)
            if (result.isSuccessful) {
                emit(State.Success(result.body()))
            } else {
                emit(State.Error(result.message()))
            }

        }
    }

    //Fun to get asteroid from Api nasa , and i will catch this data through work manager , go and see :
    suspend fun getAsteroidData(startDate: String, endDate: String, apiKey: String) {
        withContext(Dispatchers.IO) {
            val response = AsteroidObjectApi.retrofitAsteroidService.getAsteroidDetails(
                startDate,
                endDate,
                apiKey
            )
            asteroidDao.addAsteroidDetails(parseAsteroidsJsonResult(JSONObject(response)))
        }
    }

    // To get asteroid for a week :
    val getWeekAsteroid: LiveData<List<Asteroid>> = asteroidDao.getWeekAsteroids(
        getNextSevenDaysFormattedDates()[0], getNextSevenDaysFormattedDates()[6]
    )

    // To get asteroid for Today :
    val getTodayAsteroid: LiveData<List<Asteroid>> = asteroidDao.getTodayAsteroids(
        getNextSevenDaysFormattedDates()[0]
    )
}