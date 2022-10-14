package com.antonyhayek.weatherbuddy.domain.database.dao

import androidx.room.*
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAvailableCities(cities: List<City>)

    @Query("SELECT * from Cities")
    fun getAvailableCities(): Flow<List<City>>

    @Query("SELECT * from Cities WHERE id = :id")
    fun getCityById(id: Long): Flow<City>

    @Delete
    suspend fun deleteCities(cities:  List<City>)
}