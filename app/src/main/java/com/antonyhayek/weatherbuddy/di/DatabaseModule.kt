package com.antonyhayek.weatherbuddy.di

import android.content.Context
import androidx.room.Room
import com.antonyhayek.weatherbuddy.domain.database.WeatherBuddyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun weatherBuddyDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        WeatherBuddyDatabase::class.java,
        "weather_buddy_db"
    ).allowMainThreadQueries().build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideFavoriteCitiesDao(db: WeatherBuddyDatabase) = db.favoriteDao()

    @Singleton
    @Provides
    fun provideCitiesDao(db: WeatherBuddyDatabase) = db.cityDao()

    @Singleton
    @Provides
    fun provideWeatherDao(db: WeatherBuddyDatabase) = db.weatherDao()

    @Singleton
    @Provides
    fun provideForecastDao(db: WeatherBuddyDatabase) = db.forecastDao()

}
