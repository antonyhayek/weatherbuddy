package com.antonyhayek.weatherbuddy.domain.repository

import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.ForecastWeather
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather

interface WeatherRepository {

    suspend fun cityWeatherData(
        lat: Double,
        lon: Double
    ): Resource<LocationWeather>

    suspend fun dailyForecastData(
       lat: Double,
        lon: Double,
    ) : Resource<ForecastWeather>
}