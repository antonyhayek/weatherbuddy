package com.antonyhayek.weatherbuddy.domain.prefstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val STORE_NAME = "weatherbuddy_data_store"


@Singleton
class PrefsStoreImpl @Inject constructor(
    @ApplicationContext context: Context
) : PrefStore {

    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

    private val dataStore: DataStore<Preferences> = context._dataStore

    override fun getAppUnit(): Flow<String> =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { it[PreferencesKeys.UNIT] ?: "metric" }

    override suspend fun setAppUnit(unit: String) {
        dataStore.edit {
            it[PreferencesKeys.UNIT] = unit
        }
    }

    private object PreferencesKeys {
        val UNIT = stringPreferencesKey("unit")
    }


}