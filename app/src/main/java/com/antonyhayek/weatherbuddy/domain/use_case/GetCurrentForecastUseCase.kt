package com.antonyhayek.weatherbuddy.domain.use_case

import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.Coord
import com.antonyhayek.weatherbuddy.data.remote.ForecastWeather
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(coord: Coord) : Flow<Resource<ForecastWeather>> =
        weatherRepository.dailyForecastData(coord.lat, coord.lon)

}