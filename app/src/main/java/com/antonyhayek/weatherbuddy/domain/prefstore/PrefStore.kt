package com.antonyhayek.weatherbuddy.domain.prefstore

import kotlinx.coroutines.flow.Flow

interface PrefStore {

    fun getAppUnit(): Flow<String>
    suspend fun setAppUnit(unit: String)

    fun getLastUserLat(): Flow<Double>
    suspend fun setLastUserLat(lat: Double)

    fun getLastUserLon(): Flow<Double>
    suspend fun setLastUserLon(lon: Double)
}