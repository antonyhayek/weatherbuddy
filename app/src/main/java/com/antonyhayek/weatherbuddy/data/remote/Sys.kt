package com.antonyhayek.weatherbuddy.data.remote

data class Sys(
    val country: String,
    val sysId: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)