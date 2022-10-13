package com.antonyhayek.weatherbuddy.presentation.ui.citysearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.domain.use_case.FetchCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fetchCitiesUseCase: FetchCitiesUseCase
) : ViewModel() {
    private val _searchState = MutableStateFlow<UIEventSearch>(
        UIEventSearch.OnLoading(false)
    )
    var searchState = _searchState.asStateFlow()

  //  private val _temperatureFlow = MutableStateFlow(0.0)

    init {
        getCities()
    }

    private fun getCities() {
        _searchState.value = UIEventSearch.OnLoading(true)
        viewModelScope.launch {
            fetchCitiesUseCase().collect{
                when(it) {
                    is Resource.Failure -> {
                        _searchState.value = UIEventSearch.OnLoading(false)
                        _searchState.value = UIEventSearch.ShowErrorDialog(it)
                    }
                    is Resource.Loading ->  UIEventSearch.OnLoading(false)
                    is Resource.Success -> {
                        _searchState.value = UIEventSearch.OnLoading(false)
                        _searchState.value = UIEventSearch.OnCitiesRetrieved(cities = it.value)


                    }
                }
            }
        }
    }

    sealed class UIEventSearch {
        class OnLoading(var onLoading: Boolean) : UIEventSearch()
        class OnCitiesRetrieved(var cities : Cities) : UIEventSearch()
        data class ShowErrorDialog(val resourceFailure: Resource.Failure) : UIEventSearch()

    }
}