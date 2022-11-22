package com.example.asteroidradarapp

import com.squareup.moshi.Json

data class PictureOfDay(@Json(name = "media_type") var mediaType: String, var title: String,
                        var url: String)