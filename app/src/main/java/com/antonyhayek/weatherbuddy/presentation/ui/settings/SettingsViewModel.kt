package com.antonyhayek.weatherbuddy.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.Coord
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.use_case.GetAppUnitUseCase
import com.antonyhayek.weatherbuddy.domain.use_case.GetCurrentWeatherUseCase
import com.antonyhayek.weatherbuddy.domain.use_case.SetAppUnitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setAppUnitUseCase: SetAppUnitUseCase,
    private val getAppUnitUseCase: GetAppUnitUseCase
) : ViewModel() {
    private val _settingsState = MutableStateFlow<UIEventSettings>(
        UIEventSettings.OnLoading(false)
    )
    var settingsState = _settingsState.asStateFlow()

    init {
        getAppUnit()
    }

    private fun getAppUnit() {
        viewModelScope.launch{
            getAppUnitUseCase().collect {
                _settingsState.value = UIEventSettings.OnAppUnitRetrieved(it)
            }
        }

    }

    fun setAppUnit(unit: String) {
      //  _settingsState.value = UIEventSettings.OnLoading(true)
        viewModelScope.launch {
            setAppUnitUseCase(
                unit
            )
        }
    }

    sealed class UIEventSettings {
        class OnLoading(var onLoading: Boolean) : UIEventSettings()
        class OnAppUnitRetrieved(var unit : String) : UIEventSettings()
        data class ShowErrorDialog(val resourceFailure: Resource.Failure) : UIEventSettings()

    }
}