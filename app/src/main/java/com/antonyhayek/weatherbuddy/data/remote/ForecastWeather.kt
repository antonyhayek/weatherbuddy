package com.antonyhayek.weatherbuddy.data.remote

data class ForecastWeather(
    val city: ForecastCity,
    val cod: Int,
    val message: Double,
    val cnt: Int,
    val list: List<ForecastMain>
)