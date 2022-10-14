package com.antonyhayek.weatherbuddy.domain.repository

import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.data.networking.Resource
import kotlinx.coroutines.flow.Flow

interface CityRepository {

    suspend fun getCities(
    ): Flow<List<City>>

    suspend fun saveFavCity(cityToSave: FavoriteCity)

    suspend fun retrieveFavCities() : Flow<List<FavoriteCity>>

    suspend fun getFavCityIds() : Flow<List<Long>>

    suspend fun deleteFavCityById(id: Long)

    suspend fun getFavCityById(id: Long) : Flow<FavoriteCity>
}