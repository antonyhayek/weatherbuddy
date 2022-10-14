package com.antonyhayek.weatherbuddy.data.networking

import com.antonyhayek.weatherbuddy.data.remote.ForecastWeather
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST(Endpoints.WEATHER)
    suspend fun currentWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ) : LocationWeather


    @POST(Endpoints.FORECAST)
    suspend fun dailyForecastData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,

    ) : ForecastWeather
}