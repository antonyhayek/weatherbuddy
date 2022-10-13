package com.antonyhayek.weatherbuddy.domain.use_case

import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {

    operator fun invoke() : Flow<Resource<Cities>> = flow {
        emit(cityRepository.getCities())
    }
}