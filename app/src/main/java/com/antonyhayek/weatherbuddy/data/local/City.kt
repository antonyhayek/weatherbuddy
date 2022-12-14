package com.antonyhayek.weatherbuddy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cities")
data class City(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val name: String,
    val lon: Double,
    val lat: Double,
    val country: String,
    var isFavCity: Boolean
)
