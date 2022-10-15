package com.antonyhayek.weatherbuddy.presentation.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.Coord
import com.antonyhayek.weatherbuddy.data.remote.ForecastWeather
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.use_case.GetCurrentForecastUseCase
import com.antonyhayek.weatherbuddy.domain.use_case.GetCurrentWeatherUseCase
import com.antonyhayek.weatherbuddy.domain.use_case.RetrieveLastUserCoordinatesUseCase
import com.antonyhayek.weatherbuddy.domain.use_case.SetLastUserCoordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getCurrentForecastUseCase: GetCurrentForecastUseCase,
    private val retrieveLastUserCoordinatesUseCase: RetrieveLastUserCoordinatesUseCase,
    private val setLastUserCoordUseCase: SetLastUserCoordUseCase
) : ViewModel() {
    private val _currentWeatherState = MutableStateFlow<UIEventCurrentWeather>(
        UIEventCurrentWeather.OnLoading(false)
    )

    private val _forecastState = MutableStateFlow<UIEventForecast>(
        UIEventForecast.OnLoading(false)
    )

    var currentWeatherState = _currentWeatherState.asStateFlow()
    var forecastState = _forecastState.asStateFlow()

    private val _currentUserCoordFlow = MutableStateFlow(Coord(0.0,0.0))
    private val _temperatureFlow = MutableStateFlow(0.0)



    fun getCurrentWeather() {
        _currentWeatherState.value = UIEventCurrentWeather.OnLoading(true)
        viewModelScope.launch {
            getCurrentWeatherUseCase(
                _currentUserCoordFlow.value
            ).collect{
                when(it) {
                    is Resource.Failure -> {
                        _currentWeatherState.value = UIEventCurrentWeather.OnLoading(false)
                    //    _currentWeatherState.value = UIEventCurrentWeather.ShowErrorDialog(it)
                    }
                    is Resource.Loading ->  UIEventCurrentWeather.OnLoading(false)
                    is Resource.Success -> {
                        _currentWeatherState.value = UIEventCurrentWeather.OnLoading(false)
                        _currentWeatherState.value = UIEventCurrentWeather.OnCurrentWeatherRetrieved(weather = it.value)


                    }
                }
            }
        }
    }


    fun getCurrentForecast() {

        _forecastState.value = UIEventForecast.OnLoading(true)
        viewModelScope.launch {
            getCurrentForecastUseCase(
                _currentUserCoordFlow.value
            ).collect{
                when(it) {
                    is Resource.Failure -> {
                        _forecastState.value = UIEventForecast.OnLoading(false)
                   //     _forecastState.value = UIEventForecast.ShowErrorDialog(it)
                    }
                    is Resource.Loading ->  UIEventForecast.OnLoading(false)
                    is Resource.Success -> {
                        _forecastState.value = UIEventForecast.OnLoading(false)
                        _forecastState.value = UIEventForecast.OnCurrentForecastRetrieved(forecast = it.value)

                    }
                }
            }
        }
    }

    fun setUserCoord(coord: Coord) {
        _currentUserCoordFlow.value = coord
        viewModelScope.launch {
            setLastUserCoordUseCase(coord)
        }

    }

    fun getLastUserCoordinates() {
        viewModelScope.launch {
            retrieveLastUserCoordinatesUseCase().collect{
                _currentWeatherState.value = UIEventCurrentWeather.OnLastUserCoordRetrieved(coord = it)
            }
        }

    }


    sealed class UIEventCurrentWeather {
        class OnLoading(var onLoading: Boolean) : UIEventCurrentWeather()
        class OnCurrentWeatherRetrieved(var weather : LocationWeather) : UIEventCurrentWeather()
        data class ShowErrorDialog(val resourceFailure: Resource.Failure) : UIEventCurrentWeather()
        class OnLastUserCoordRetrieved(var coord: Coord) : UIEventCurrentWeather()
    }

    sealed class UIEventForecast {
        class OnLoading(var onLoading: Boolean) : UIEventForecast()
        data class ShowErrorDialog(val resourceFailure: Resource.Failure) : UIEventForecast()
        class OnCurrentForecastRetrieved(var forecast : ForecastWeather) : UIEventForecast()
    }

}