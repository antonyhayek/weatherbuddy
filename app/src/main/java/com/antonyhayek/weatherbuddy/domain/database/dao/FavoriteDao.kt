package com.antonyhayek.weatherbuddy.domain.database.dao

import androidx.room.*
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCity(city: FavoriteCity)

    @Query("SELECT * from FavCities WHERE id = :id")
    fun getFavCityById(id: Long): Flow<FavoriteCity>

    @Query("SELECT * from FavCities")
    fun getFavoriteCities(): Flow<List<FavoriteCity>>

    @Query("SELECT id from FavCities")
    fun getFavoriteCitiesIds(): Flow<List<Long>>

    @Delete
    suspend fun deleteFavoriteCities(cities:  List<FavoriteCity>)

    @Query("DELETE FROM FavCities WHERE id = :id")
    suspend fun deleteFavoriteById(id: Long)


}