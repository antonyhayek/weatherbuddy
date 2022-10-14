package com.antonyhayek.weatherbuddy.data.remote

data class ForecastMain(
    val dt: Long,
    val main: Main,
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val id: Int,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind,
    val pop: Double,
    val dt_txt: String
  /*  val sunrise: Int,
    val sunset: Int,
    val temp: Temperature,
    val feels_like: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    val weather: List<Weather>,
    val deg: Int,
    val gust: Double,
    val speed: Double,
    val clouds: Int,
    val pop: Double,
    val rain: Double*/
)