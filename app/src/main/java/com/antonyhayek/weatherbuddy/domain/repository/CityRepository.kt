package com.antonyhayek.weatherbuddy.domain.repository

import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.networking.Resource

interface CityRepository {

    suspend fun getCities(
    ): Resource<Cities>

}