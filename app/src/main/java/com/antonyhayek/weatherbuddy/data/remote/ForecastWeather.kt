package com.antonyhayek.weatherbuddy.data.remote

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Forecast")
data class ForecastWeather(
    @PrimaryKey(autoGenerate = true)
    val forecastId: Int,
    @Embedded
    val city: ForecastCity,
    val cod: Int,
    val message: Double,
    val cnt: Int,
    val list: List<ForecastMain>
)