package com.antonyhayek.weatherbuddy.data.repository

import com.antonyhayek.weatherbuddy.data.networking.ApiService
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private var apiService: ApiService
) : WeatherRepository, BaseRepository() {

    override suspend fun cityWeatherData(lat: Double, lon: Double): Resource<LocationWeather> = safeApiCall {
        apiService.currentWeatherData(lat, lon)
    }

}