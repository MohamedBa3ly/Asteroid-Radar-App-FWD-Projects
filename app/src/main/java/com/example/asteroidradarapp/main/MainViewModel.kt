package com.example.asteroidradarapp.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asteroidradarapp.Asteroid
import com.example.asteroidradarapp.PictureOfDay
import com.example.asteroidradarapp.State
import com.example.asteroidradarapp.repository.AsteroidRoomRebo
import com.example.asteroidradarapp.roomdatabase.AsteroidDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    //Make an object from (AsteroidRoomRebo) to use all fun from it :
    private val repository: AsteroidRoomRebo

    //Make an object to be livedata<list<Asteroid>> , first should be init :
    var readAllAsteroidDetails: LiveData<List<Asteroid>>

    //Make also for image :
    var customImage: MutableLiveData<State<PictureOfDay?>> = MutableLiveData()

    //Make object from getTodayAsteroid :
    var getTodayAsteroid: LiveData<List<Asteroid>>

    //Make object from getWeekAsteroid:
    var getWeekAsteroid: LiveData<List<Asteroid>>


    //Initialize :
    init {
        val db = AsteroidDataBase.getInstance(application)
        val asteroidDao = db.asteroidDao()
        repository = AsteroidRoomRebo(asteroidDao)
        readAllAsteroidDetails = repository.readAllAsteroidsDetails

        //Get today Asteroid:
        getTodayAsteroid = repository.getTodayAsteroid

        //Get week Asteroid:
        getWeekAsteroid = repository.getWeekAsteroid
    }

    //Fun to add asteroids and i use a coroutine scope ( for learning) :
    fun addAsteroidDetails(asteroid: List<Asteroid>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAsteroidDetails(asteroid)
        }
    }

    //Fun to update asteroids and i use a coroutine scope ( for learning):
    fun updateAsteroidDetails(asteroid: Asteroid) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAsteroidDetails(asteroid)
        }
    }

    //Fun to delete asteroids and i use a coroutine scope (for learning):
    fun deleteAsteroidDetails(asteroid: Asteroid) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAsteroidDetails(asteroid)
        }
    }

    //Fun to delete all asteroids and i use a coroutine scope (for learning) :
    fun deleteAllAsteroidDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAsteroidDetails()
        }
    }

    //Fun to Get the picture of the day :
    fun getPictureFinally(apiKey: String) {
        viewModelScope.launch {
            repository.getImageOfDays(apiKey).collect {
                customImage.postValue(it)
            }
        }
    }



}