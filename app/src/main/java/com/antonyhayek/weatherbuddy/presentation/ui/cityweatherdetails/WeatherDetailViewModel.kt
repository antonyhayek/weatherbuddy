package com.antonyhayek.weatherbuddy.presentation.ui.cityweatherdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.use_case.FetchCitiesUseCase
import com.antonyhayek.weatherbuddy.domain.use_case.GetCityWeatherUseCase
import com.antonyhayek.weatherbuddy.presentation.ui.dashboard.DashboardViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    private val getCityWeatherUseCase: GetCityWeatherUseCase
) : ViewModel() {
    private val _weatherDetailState = MutableStateFlow<UIEventWeatherDetail>(
        UIEventWeatherDetail.OnLoading(false)
    )
    var weatherDetailState = _weatherDetailState.asStateFlow()

    //  private val _temperatureFlow = MutableStateFlow(0.0)

    fun getCityWeather(lat: Double, lon: Double) {
        _weatherDetailState.value = UIEventWeatherDetail.OnLoading(true)
        viewModelScope.launch {
            getCityWeatherUseCase(lat, lon).collect {
                when(it) {
                    is Resource.Failure -> {
                        _weatherDetailState.value = UIEventWeatherDetail.OnLoading(false)
                    //    _weatherDetailState.value = UIEventWeatherDetail.ShowErrorDialog(it)
                    }
                    is Resource.Loading ->  UIEventWeatherDetail.OnLoading(false)
                    is Resource.Success -> {
                        _weatherDetailState.value = UIEventWeatherDetail.OnLoading(false)
                        _weatherDetailState.value = UIEventWeatherDetail.OnWeatherDetailRetrieved(weather = it.value)


                    }
                }
            }
        }
    }

    sealed class UIEventWeatherDetail {
        class OnLoading(var onLoading: Boolean) : UIEventWeatherDetail()
        class OnWeatherDetailRetrieved(var weather: LocationWeather) : UIEventWeatherDetail()
        data class ShowErrorDialog(val resourceFailure: Resource.Failure) : UIEventWeatherDetail()

    }
}