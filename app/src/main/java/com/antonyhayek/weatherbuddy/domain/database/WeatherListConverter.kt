package com.antonyhayek.weatherbuddy.domain.database

import androidx.room.TypeConverter
import com.antonyhayek.weatherbuddy.data.remote.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherListConverter {
    @TypeConverter
    fun fromWeatherList(events: List<Weather>): String {

        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.toJson(events, type)
    }

    @TypeConverter
    fun toWeatherList(eventString: String) :  List<Weather>{

        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(eventString, type)
    }
}