package com.antonyhayek.weatherbuddy.domain.prefstore

import kotlinx.coroutines.flow.Flow

interface PrefStore {

    fun getAppUnit(): Flow<String>
    suspend fun setAppUnit(unit: String)
}