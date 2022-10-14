package com.antonyhayek.weatherbuddy.data.repository

import android.content.Context
import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.domain.database.dao.CityDao
import com.antonyhayek.weatherbuddy.domain.database.dao.FavoriteDao
import com.antonyhayek.weatherbuddy.domain.repository.CityRepository
import com.antonyhayek.weatherbuddy.utils.JsonUtils
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import javax.inject.Inject


class CityRepositoryImpl @Inject constructor(
    private var context: Context,
    private var cityDao: CityDao,
    private var favoriteDao: FavoriteDao
) : CityRepository, BaseRepository() {

    override suspend fun getCities(): Flow<List<City>> {
        val obj = JSONObject(JsonUtils.loadJSONFromAsset(context))

        val cityList = Resource.Success(
            Gson().fromJson(
                obj.toString(),
                Cities::class.java
            ) as Cities
        )

        if (cityDao.getAvailableCities().first().isEmpty()) {
            runBlocking {
                cityDao.insertAvailableCities(cityList.value.cities_list)
            }
        }

        return cityDao.getAvailableCities()
    }

    override suspend fun saveFavCity(cityToSave: FavoriteCity) {
        favoriteDao.insertFavoriteCity(cityToSave)
    }

    override suspend fun retrieveFavCities(): Flow<List<FavoriteCity>> {
        return favoriteDao.getFavoriteCities()
    }

    override suspend fun getFavCityIds(): Flow<List<Long>> {
        return favoriteDao.getFavoriteCitiesIds()
    }

    override suspend fun deleteFavCityById(id: Long) {
        favoriteDao.deleteFavoriteById(id)
    }

    override suspend fun getFavCityById(id: Long): Flow<FavoriteCity> {
        return favoriteDao.getFavCityById(id)
    }

}