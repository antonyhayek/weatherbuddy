package com.antonyhayek.weatherbuddy.data.remote

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Weather")
data class LocationWeather(
    val base: String,
    @Embedded
    val clouds: Clouds,
    val cod: Int,
    @Embedded
    val coord: Coord,
    val dt: Int,
    @PrimaryKey
    val id: Int,
    @Embedded
    val main: Main,
    val name: String,
    @Embedded
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    @Embedded
    val wind: Wind
)