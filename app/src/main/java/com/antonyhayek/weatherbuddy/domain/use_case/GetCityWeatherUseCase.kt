package com.antonyhayek.weatherbuddy.domain.use_case

import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCityWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(lat: Double, lon: Double) : Flow<Resource<LocationWeather>> =
        weatherRepository.cityWeatherData(lat, lon)

}