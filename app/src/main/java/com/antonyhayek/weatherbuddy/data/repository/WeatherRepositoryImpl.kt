package com.antonyhayek.weatherbuddy.data.repository

import android.content.Context
import androidx.room.withTransaction
import com.antonyhayek.weatherbuddy.data.networking.ApiService
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.networking.networkBoundResource
import com.antonyhayek.weatherbuddy.data.remote.ForecastWeather
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.database.WeatherBuddyDatabase
import com.antonyhayek.weatherbuddy.domain.database.dao.ForecastDao
import com.antonyhayek.weatherbuddy.domain.database.dao.WeatherDao
import com.antonyhayek.weatherbuddy.domain.repository.WeatherRepository
import com.antonyhayek.weatherbuddy.utils.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private var apiService: ApiService,
    private var weatherDao: WeatherDao,
    private var forecastDao: ForecastDao,
    private var database: WeatherBuddyDatabase,
    private var context: Context
) : WeatherRepository, BaseRepository() {

    /*override suspend fun cityWeatherData(lat: Double, lon: Double): Resource<LocationWeather> = safeApiCall {
        apiService.currentWeatherData(lat, lon)
    }*/

    override suspend fun cityWeatherData(lat: Double, lon: Double) = networkBoundResource(
        query = {
            weatherDao.getLocalWeather()
        },
        fetch = {
            delay(500)
            apiService.currentWeatherData(lat, lon)
        },
        saveFetchResult = { weather ->
            database.withTransaction {
                weatherDao.deleteLocalWeather()
                weatherDao.insertLocalWeather(weather)
            }
        },
        shouldFetch = { NetworkUtils.isInternetAvailable(context) }
    )


    override suspend fun dailyForecastData(lat: Double, lon: Double) = networkBoundResource(
        query = {
            forecastDao.getLocalForecast()
        },
        fetch = {
            delay(500)
            apiService.dailyForecastData(lat, lon)
        },
        saveFetchResult = { forecast ->
            database.withTransaction {
                forecastDao.deleteLocalForecast()
                forecastDao.insertLocalForecast(forecast)
            }
        },
        shouldFetch = { NetworkUtils.isInternetAvailable(context) }
    )

}