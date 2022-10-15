package com.antonyhayek.weatherbuddy.domain.repository

import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.Coord

interface SettingsRepository {

    suspend fun getAppUnit(
    ): String

    suspend fun setAppUnit(unit: String)

    suspend fun getLastUserCoord(): Coord

    suspend fun setLastUserLat(lat: Double)

    suspend fun setLastUserLon(lon: Double)
}