package com.antonyhayek.weatherbuddy.presentation.ui.citysearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonyhayek.weatherbuddy.data.local.Cities
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.data.networking.Resource
import com.antonyhayek.weatherbuddy.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fetchCitiesUseCase: FetchCitiesUseCase,
    private val getFavoriteByIdUseCase: RetrieveFavoriteByIdUseCase,
    private val deleteFavoriteCityUseCase: DeleteFavoriteCityUseCase,
    private val insertFavoriteCityUseCase: InsertFavoriteCityUseCase,
    private val retrieveFavoriteCityIdsUseCase: RetrieveFavoriteCityIdsUseCase
) : ViewModel() {
    private val _searchState = MutableStateFlow<UIEventSearch>(
        UIEventSearch.OnLoading(false)
    )
    var searchState = _searchState.asStateFlow()

  //  private val _temperatureFlow = MutableStateFlow(0.0)

   /* init {
        getCities()
    }*/

    fun getCities() {
        _searchState.value = UIEventSearch.OnLoading(true)
        viewModelScope.launch {
            fetchCitiesUseCase().collect{

                _searchState.value = UIEventSearch.OnLoading(false)
                _searchState.value = UIEventSearch.OnCitiesRetrieved(cities = it)

               /* when(it) {
                    is Resource.Failure -> {
                        _searchState.value = UIEventSearch.OnLoading(false)
                        _searchState.value = UIEventSearch.ShowErrorDialog(it)
                    }
                    is Resource.Loading ->  UIEventSearch.OnLoading(false)
                    is Resource.Success -> {
                        _searchState.value = UIEventSearch.OnLoading(false)
                        _searchState.value = UIEventSearch.OnCitiesRetrieved(cities = it.value)


                    }
                }*/
            }
        }
    }

    suspend fun retrieveFavCityById(id: Long) = getFavoriteByIdUseCase(id)

    fun deleteCityById(id: Long) {
        viewModelScope.launch {
            deleteFavoriteCityUseCase(id)
        }
    }

    fun addCityToFav(city: FavoriteCity) {
        viewModelScope.launch {
            insertFavoriteCityUseCase(city)
        }
    }

    suspend fun retrieveFavCitiesIds() = retrieveFavoriteCityIdsUseCase()

    sealed class UIEventSearch {
        class OnLoading(var onLoading: Boolean) : UIEventSearch()
        class OnCitiesRetrieved(var cities : List<City>) : UIEventSearch()
        data class ShowErrorDialog(val resourceFailure: Resource.Failure) : UIEventSearch()

    }
}