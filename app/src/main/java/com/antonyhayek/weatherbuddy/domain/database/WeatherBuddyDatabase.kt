package com.antonyhayek.weatherbuddy.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.data.remote.ForecastWeather
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.data.remote.Weather
import com.antonyhayek.weatherbuddy.domain.database.dao.CityDao
import com.antonyhayek.weatherbuddy.domain.database.dao.FavoriteDao
import com.antonyhayek.weatherbuddy.domain.database.dao.ForecastDao
import com.antonyhayek.weatherbuddy.domain.database.dao.WeatherDao

@Database(
    entities = [City::class, FavoriteCity::class, LocationWeather::class, ForecastWeather::class],
    version = 1,
    exportSchema = true
)

@TypeConverters(
    WeatherListConverter::class,
    ForecastListConverter::class
)
abstract class WeatherBuddyDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun forecastDao(): ForecastDao
    abstract fun cityDao(): CityDao
    abstract fun favoriteDao(): FavoriteDao
}
