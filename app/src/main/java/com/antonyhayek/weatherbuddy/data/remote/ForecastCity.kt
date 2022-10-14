package com.antonyhayek.weatherbuddy.data.remote

data class ForecastCity(
    val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Long,
    val timeZone: Int,
    val sunrise: Int,
    val sunset: Int
)
