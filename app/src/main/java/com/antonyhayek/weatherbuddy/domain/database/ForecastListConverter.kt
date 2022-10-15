package com.antonyhayek.weatherbuddy.domain.database

import androidx.room.TypeConverter
import com.antonyhayek.weatherbuddy.data.remote.ForecastMain
import com.antonyhayek.weatherbuddy.data.remote.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ForecastListConverter {
    @TypeConverter
    fun fromForecastList(events: List<ForecastMain>): String {

        val gson = Gson()
        val type = object : TypeToken<List<ForecastMain>>() {}.type
        return gson.toJson(events, type)
    }

    @TypeConverter
    fun toForecastList(eventString: String) :  List<ForecastMain>{

        val gson = Gson()
        val type = object : TypeToken<List<ForecastMain>>() {}.type
        return gson.fromJson(eventString, type)
    }
}