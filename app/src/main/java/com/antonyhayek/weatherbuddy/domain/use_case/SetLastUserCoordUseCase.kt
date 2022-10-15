package com.antonyhayek.weatherbuddy.domain.use_case

import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.Coord
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.repository.SettingsRepository
import com.antonyhayek.weatherbuddy.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetLastUserCoordUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(coord: Coord) = run {
        settingsRepository.setLastUserLat(coord.lat)
        settingsRepository.setLastUserLon(coord.lon)
    }

}