package com.antonyhayek.weatherbuddy.data.remote

import androidx.room.Embedded
import androidx.room.PrimaryKey

data class ForecastCity(
    @PrimaryKey
    val id: Long,
    val name: String,
    @Embedded
    val coord: Coord,
    val country: String,
    val population: Long,
    val timeZone: Int,
    val sunrise: Int,
    val sunset: Int
)
