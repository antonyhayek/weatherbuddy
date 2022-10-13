package com.antonyhayek.weatherbuddy.presentation.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.Coord
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.use_case.GetCurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModel() {
    private val _dashboardState = MutableStateFlow<UIEventDashboard>(
        UIEventDashboard.OnLoading(false)
    )
    var dashboardState = _dashboardState.asStateFlow()

    private val _currentUserCoordFlow = MutableStateFlow(Coord(0.0,0.0))
    private val _temperatureFlow = MutableStateFlow(0.0)



    fun getCurrentWeather() {
        _dashboardState.value = UIEventDashboard.OnLoading(true)
        viewModelScope.launch {
            getCurrentWeatherUseCase(
                _currentUserCoordFlow.value
            ).collect{
                when(it) {
                    is Resource.Failure -> {
                        _dashboardState.value = UIEventDashboard.OnLoading(false)
                        _dashboardState.value = UIEventDashboard.ShowErrorDialog(it)
                    }
                    is Resource.Loading ->  UIEventDashboard.OnLoading(false)
                    is Resource.Success -> {
                        _dashboardState.value = UIEventDashboard.OnLoading(false)
                        _dashboardState.value = UIEventDashboard.OnCurrentWeatherRetrieved(weather = it.value)


                    }
                }
            }
        }
    }

    fun setUserCoord(coord: Coord) {
        _currentUserCoordFlow.value = coord
    }

    sealed class UIEventDashboard {
        class OnLoading(var onLoading: Boolean) : UIEventDashboard()
        class OnCurrentWeatherRetrieved(var weather : LocationWeather) : UIEventDashboard()
        data class ShowErrorDialog(val resourceFailure: Resource.Failure) : UIEventDashboard()

    }
}