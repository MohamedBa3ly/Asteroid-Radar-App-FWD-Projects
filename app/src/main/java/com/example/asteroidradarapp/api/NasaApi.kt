package com.example.asteroidradarapp.api

import com.example.asteroidradarapp.Asteroid
import com.example.asteroidradarapp.Constants
import com.example.asteroidradarapp.PictureOfDay
import com.example.asteroidradarapp.State
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//interface for Asteroid :
interface AsteroidApi {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidDetails(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): String
}

//Interface for picture of day :
interface PictureOfDayApi {
    @GET("planetary/apod")
    suspend fun getImageOfDay(
        @Query("api_key") apiKey: String
    ): Response<PictureOfDay>
}

// Make an object for Asteroid :
object AsteroidObjectApi {
    var retrofitAsteroidService: AsteroidApi = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
//        .addConverterFactory(
//            MoshiConverterFactory.create(
//                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//            )
//        )
        .baseUrl(Constants.BASE_URL)
        .build()
        .create(AsteroidApi::class.java)
}

//Make an object for picture :
object PictureOfDayObjectApi {
    var retrofitPictureService: PictureOfDayApi = Retrofit.Builder()
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
        .baseUrl(Constants.BASE_URL)
        .build()
        .create(PictureOfDayApi::class.java)
}






