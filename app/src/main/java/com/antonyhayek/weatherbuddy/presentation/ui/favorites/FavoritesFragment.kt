package com.antonyhayek.weatherbuddy.presentation.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.databinding.FragmentFavoritesBinding
import com.antonyhayek.weatherbuddy.presentation.base.BaseFragment
import com.antonyhayek.weatherbuddy.presentation.ui.citysearch.SearchFragmentDirections
import com.antonyhayek.weatherbuddy.presentation.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {
    private lateinit var favCitiesAdapter: FavCitiesAdapter
    private val viewModel: FavoritesViewModel by viewModels()
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutListeners()
        setupFavCitiesAdapter()
        collectFavoriteCities()
    }

    private fun setupFavCitiesAdapter() {

        favCitiesAdapter = FavCitiesAdapter(
            onFavCityClick = { position, city ->
                onCityClicked(city)
            },
            onRemoveFavClicked = { position, city ->
                onRemoveFav(city)
            }
        )

        binding.rvFavorites.adapter = favCitiesAdapter
    }

    private fun onCityClicked(city: FavoriteCity) {
        if (findNavController().currentDestination!!.id == R.id.favoritesFragment)
            findNavController().navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToWeatherDetailsFragment(
                    city.lat.toFloat(),
                    city.lon.toFloat()
                )
            )
    }

    private fun onRemoveFav(city: FavoriteCity) {
        viewModel.removeFavCity(city.id)
    }

    private fun collectFavoriteCities() {

        job = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            lifecycleScope.launchWhenCreated {
                viewModel.favoritesState.collect { uiState ->
                    when (uiState) {
                        is FavoritesViewModel.UIEventFavorites.OnLoading -> {
                            if (uiState.onLoading)
                                showLoading()
                        }
                        is FavoritesViewModel.UIEventFavorites.OnFavoriteCitiesRetrieved -> {
                            hideLoading()

                            favCitiesAdapter.setFavCities(uiState.favoriteCities)
                        }
                        is FavoritesViewModel.UIEventFavorites.ShowErrorDialog -> {
                            hideLoadingDialog()

                            showErrorDialog({ }, uiState.resourceFailure, true)
                        }

                    }
                }
            }
        }
    }

    private fun setLayoutListeners() {

        with(binding) {

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }

}