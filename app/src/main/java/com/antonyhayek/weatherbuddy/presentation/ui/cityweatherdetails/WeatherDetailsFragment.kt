package com.antonyhayek.weatherbuddy.presentation.ui.cityweatherdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.databinding.FragmentWeatherDetailsBinding
import com.antonyhayek.weatherbuddy.presentation.base.BaseFragment
import com.antonyhayek.weatherbuddy.presentation.ui.dashboard.DashboardViewModel
import com.antonyhayek.weatherbuddy.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class WeatherDetailsFragment : BaseFragment<FragmentWeatherDetailsBinding>(FragmentWeatherDetailsBinding::inflate) {
    private val args: WeatherDetailsFragmentArgs by navArgs()
    private val viewModel: WeatherDetailViewModel by viewModels()
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCityWeather(args.lat.toDouble(), args.lon.toDouble())

        collectWeatherData()
        setLayoutListeners()
    }

    private fun setLayoutListeners() {

        with(binding) {
            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun collectWeatherData() {

        job = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            lifecycleScope.launchWhenCreated {
                viewModel.weatherDetailState.collect { uiState ->
                    when (uiState) {
                        is WeatherDetailViewModel.UIEventWeatherDetail.OnLoading -> {
                            if (uiState.onLoading)
                                showLoading()
                        }
                        is WeatherDetailViewModel.UIEventWeatherDetail.OnWeatherDetailRetrieved -> {
                            hideLoading()

                            binding.currentWeather = uiState.weather
                            binding.tvTemperature.text =
                                uiState.weather.main.temp.toInt().toString()
                            ImageUtils.downloadImage(
                                requireContext(),
                                uiState.weather.weather[0].icon,
                                binding.ivWeatherIcon,
                                0
                            )
                        }
                        is WeatherDetailViewModel.UIEventWeatherDetail.ShowErrorDialog -> {
                            hideLoadingDialog()

                            showErrorDialog({ }, uiState.resourceFailure, true)
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
}