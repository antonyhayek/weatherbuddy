package com.antonyhayek.weatherbuddy.domain.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.data.remote.ForecastWeather
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalForecast(forecastWeather: ForecastWeather)

    @Query("SELECT * from Forecast")
    fun getLocalForecast(): Flow<ForecastWeather>

    @Query("DELETE FROM Forecast")
    suspend fun deleteLocalForecast()
}