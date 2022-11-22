package com.example.asteroidradarapp.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asteroidradarapp.Asteroid

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDataBase: RoomDatabase() {
    abstract fun asteroidDao(): AsteroidDao

    companion object{
        @Volatile
        private var Instance : AsteroidDataBase?=null
        fun getInstance(context: Context):AsteroidDataBase{
            val tempInstance = Instance
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    AsteroidDataBase::class.java,
                    "Asteroid_DataBase").build()
                Instance = instance
                return instance
            }
        }
    }
}