package com.antonyhayek.weatherbuddy.data.repository

import android.content.Context
import android.service.autofill.UserData
import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.networking.ApiService
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.domain.prefstore.PrefsStoreImpl
import com.antonyhayek.weatherbuddy.domain.repository.CityRepository
import com.antonyhayek.weatherbuddy.domain.repository.SettingsRepository
import com.antonyhayek.weatherbuddy.utils.JsonUtils
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject


class SettingsRepositoryImpl @Inject constructor(
    private var prefsStoreImpl: PrefsStoreImpl
) : SettingsRepository, BaseRepository() {

    override suspend fun getAppUnit(): String {
        return prefsStoreImpl.getAppUnit().first()
    }

    override suspend fun setAppUnit(unit: String) {
        prefsStoreImpl.setAppUnit(unit)
    }


}