package com.antonyhayek.weatherbuddy.domain.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalWeather(weather: LocationWeather)

    @Query("SELECT * from Weather")
    fun getLocalWeather(): Flow<LocationWeather>

    @Query("DELETE FROM Weather")
    suspend fun deleteLocalWeather()
}