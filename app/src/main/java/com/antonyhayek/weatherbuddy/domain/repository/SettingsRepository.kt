package com.antonyhayek.weatherbuddy.domain.repository

import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.networking.Resource

interface SettingsRepository {

    suspend fun getAppUnit(
    ): String

    suspend fun setAppUnit(unit: String)

}