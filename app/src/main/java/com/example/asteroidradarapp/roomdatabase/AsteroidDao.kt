package com.example.asteroidradarapp.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asteroidradarapp.Asteroid

@Dao
interface AsteroidDao {
    //To insert data in Asteroid data base :
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAsteroidDetails(asteroid:List<Asteroid>)

    //To update data in Asteroid data base (i don't need it now but for learning):
    @Update
    suspend fun updateAsteroidDetails(asteroid: Asteroid)

    //To delete data in Asteroid data base (i don't need it now but for learning):
    @Delete
    suspend fun deleteAsteroidDetails(asteroid: Asteroid)

    //To delete all data in Asteroid data base ( i don't need it now but for learning):
    @Query("DELETE FROM asteroid_table")
    suspend fun deleteAllAsteroidDetails()

    //To read all data in Asteroid data base :
    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate ASC")
    fun readAllAsteroidDetails() : LiveData<List<Asteroid>>

    //To get asteroid for a week :
    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate  BETWEEN :startDate AND :endDate ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(startDate: String, endDate: String): LiveData<List<Asteroid>>

    //To get asteroid for Today :
    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate == :todayDate ORDER BY closeApproachDate ASC ")
    fun getTodayAsteroids(todayDate: String): LiveData<List<Asteroid>>

    //To delete previous day asteroid :
    @Query("DELETE FROM asteroid_table WHERE closeApproachDate == :date")
    fun deletePreviousDayAsteroids(date: String)
}