package com.antonyhayek.weatherbuddy.data.repository

import android.content.Context
import android.service.autofill.UserData
import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.networking.ApiService
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.domain.repository.CityRepository
import com.antonyhayek.weatherbuddy.utils.JsonUtils
import com.google.gson.Gson
import org.json.JSONObject
import javax.inject.Inject


class CityRepositoryImpl @Inject constructor(
    private var context: Context
) : CityRepository, BaseRepository() {

    override suspend fun getCities(): Resource<Cities> {
        val obj = JSONObject(JsonUtils.loadJSONFromAsset(context))
        return Resource.Success(Gson().fromJson(
            obj.toString(),
            Cities::class.java
        ) as Cities)
    }

}