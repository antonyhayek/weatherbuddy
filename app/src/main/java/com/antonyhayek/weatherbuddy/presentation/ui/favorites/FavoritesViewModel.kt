package com.antonyhayek.weatherbuddy.presentation.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.data.remote.Coord
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val retrieveFavoriteCitiesUseCase: RetrieveFavoriteCitiesUseCase,
    private val deleteFavoriteCityUseCase: DeleteFavoriteCityUseCase,
    private val insertFavoriteCityListFromCSVUseCase: InsertFavoriteCityListFromCSVUseCase
) : ViewModel() {
    private val _favoritesState = MutableStateFlow<UIEventFavorites>(
        UIEventFavorites.OnLoading(false)
    )
    var favoritesState = _favoritesState.asStateFlow()

    init {
        retrieveFavoriteCities()
    }

    private fun retrieveFavoriteCities() {
        _favoritesState.value = UIEventFavorites.OnLoading(true)
        viewModelScope.launch {
            retrieveFavoriteCitiesUseCase().collect {
                _favoritesState.value = UIEventFavorites.OnLoading(false)
                _favoritesState.value = UIEventFavorites.OnFavoriteCitiesRetrieved(it)
            }
        }

    }

    fun removeFavCity(id: Long) {
        viewModelScope.launch {
            deleteFavoriteCityUseCase(id)
            retrieveFavoriteCities()
        }
    }

    fun importCitiesFromCSV(columns: StringBuilder, value: StringBuilder) {
        viewModelScope.launch {
            insertFavoriteCityListFromCSVUseCase(columns, value)
        }
    }


    sealed class UIEventFavorites {
        class OnLoading(var onLoading: Boolean) : UIEventFavorites()
        class OnFavoriteCitiesRetrieved(var favoriteCities: List<FavoriteCity>) : UIEventFavorites()
        data class ShowErrorDialog(val resourceFailure: Resource.Failure) : UIEventFavorites()

    }
}