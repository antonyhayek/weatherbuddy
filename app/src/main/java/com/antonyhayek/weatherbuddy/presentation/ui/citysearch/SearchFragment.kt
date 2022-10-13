package com.antonyhayek.weatherbuddy.presentation.ui.citysearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.databinding.FragmentSearchBinding
import com.antonyhayek.weatherbuddy.presentation.base.BaseFragment
import com.antonyhayek.weatherbuddy.presentation.ui.dashboard.DashboardViewModel
import com.antonyhayek.weatherbuddy.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private lateinit var citiesAdapter: CitiesAdapter
    private var job: Job? = null
    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutListeners()
        setupCitiesRecyclerView()
        collectSearchData()
    }

    private fun setupCitiesRecyclerView() {

        citiesAdapter = CitiesAdapter(
            onCityClicked = { position, city ->

                if (findNavController().currentDestination!!.id == R.id.searchFragment)
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToWeatherDetailsFragment(
                            city.lat.toFloat(),
                            city.lon.toFloat()
                        )
                    )
            }
        )

        binding.rvCities.adapter = citiesAdapter
    }

    private fun collectSearchData() {

        job = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            lifecycleScope.launchWhenCreated {
                viewModel.searchState.collect { uiState ->
                    when (uiState) {
                        is SearchViewModel.UIEventSearch.OnLoading -> {
                            if (uiState.onLoading)
                                showLoading()
                        }
                        is SearchViewModel.UIEventSearch.OnCitiesRetrieved -> {
                            hideLoading()

                            //TODO: populate cities.json recyclerview
                            citiesAdapter.setCities(uiState.cities.cities_list)
                        }
                        is SearchViewModel.UIEventSearch.ShowErrorDialog -> {
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

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (::citiesAdapter.isInitialized) {
                        citiesAdapter.filter.filter(s.toString())
                    }
                }

            })
        }
    }


    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }

}