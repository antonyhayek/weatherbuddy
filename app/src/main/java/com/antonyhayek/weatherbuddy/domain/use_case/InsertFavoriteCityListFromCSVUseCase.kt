package com.antonyhayek.weatherbuddy.domain.use_case

import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.Coord
import com.antonyhayek.weatherbuddy.data.remote.ForecastWeather
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.repository.CityRepository
import com.antonyhayek.weatherbuddy.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertFavoriteCityListFromCSVUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {

    suspend operator fun invoke(columns: StringBuilder, value: StringBuilder) = cityRepository.saveFavCitiesFromCSV(columns, value)
}