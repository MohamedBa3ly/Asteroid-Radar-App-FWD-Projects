package com.example.asteroidradarapp

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.asteroidradarapp.api.*
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@Entity(tableName = "asteroid_table")
data class Asteroid(
    @PrimaryKey var id: Long,
    var codename: String,
    var closeApproachDate:String,
    var absoluteMagnitude: Double,
    var estimatedDiameter: Double,
    var relativeVelocity: Double,
    var distanceFromEarth: Double,
    var isPotentiallyHazardous: Boolean
) : Parcelable





