package com.antonyhayek.weatherbuddy.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.domain.database.dao.CityDao
import com.antonyhayek.weatherbuddy.domain.database.dao.FavoriteDao

@Database(
    entities = [City::class, FavoriteCity::class],
    version = 1,
    exportSchema = true
)
abstract class WeatherBuddyDatabase: RoomDatabase() {
/*    abstract fun weatherDao(): WeatherDao
    abstract fun forecastDao(): ForecastDao*/
    abstract fun cityDao(): CityDao
    abstract fun favoriteDao(): FavoriteDao
}
