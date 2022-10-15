package com.antonyhayek.weatherbuddy.presentation.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.data.remote.Coord
import com.antonyhayek.weatherbuddy.databinding.FragmentDashboardBinding
import com.antonyhayek.weatherbuddy.presentation.base.BaseFragment
import com.antonyhayek.weatherbuddy.presentation.ui.citysearch.CitiesAdapter
import com.antonyhayek.weatherbuddy.presentation.ui.citysearch.SearchFragmentDirections
import com.antonyhayek.weatherbuddy.utils.ImageUtils
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>(FragmentDashboardBinding::inflate) {
    private lateinit var forecastAdapter: ForecastAdapter
    private val viewModel: DashboardViewModel by viewModels()
    private var job: Job? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var builder: LocationSettingsRequest.Builder


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutListeners()
        collectWeatherData()
        setupForecastRecyclerView()

        requestLocationPermission()
    }

    private fun setupForecastRecyclerView() {
        forecastAdapter = ForecastAdapter()
        binding.rvNextDays.adapter = forecastAdapter
    }

    private fun requestLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED

            || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            Dexter.withContext(requireContext())
                .withPermissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {

                            createLocationRequest()

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .withErrorListener {
                    requestLocationPermission()
                }
                .onSameThread()
                .check()
        } else {
            createLocationRequest()
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY

        // Create LocationSettingsRequest object using location request
        builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        settingsClient = LocationServices.getSettingsClient(requireContext())

        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {

            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(e.resolution).build()

                    requestCheckSettings.launch(intentSenderRequest)


                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun collectWeatherData() {

        job = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            lifecycleScope.launchWhenCreated {
                launch {

                    viewModel.currentWeatherState.collect { uiState ->
                        when (uiState) {
                            is DashboardViewModel.UIEventCurrentWeather.OnLoading -> {
                                if (uiState.onLoading)
                                    showLoading()
                            }
                            is DashboardViewModel.UIEventCurrentWeather.OnCurrentWeatherRetrieved -> {
                                hideLoading()

                                if (isAdded && isVisible) {
                                    binding.currentWeather = uiState.weather
                                    binding.tvTemperature.text =
                                        uiState.weather.main.temp.toInt().toString()
                                    ImageUtils.downloadImage(
                                        requireContext(),
                                        uiState.weather.weather[0].icon,
                                        binding.ivWeatherIcon,
                                        R.drawable.ic_error_default
                                    )

                                    binding.lytWeatherInfo.isVisible = true
                                }
                            }
                            is DashboardViewModel.UIEventCurrentWeather.ShowErrorDialog -> {
                                hideLoadingDialog()

                                showErrorDialog({ }, uiState.resourceFailure, true)
                            }
                            is DashboardViewModel.UIEventCurrentWeather.OnLastUserCoordRetrieved -> {

                                if(uiState.coord.lat != 0.0 && uiState.coord.lon != 0.0) {
                                    viewModel.setUserCoord(coord = uiState.coord)
                                    viewModel.getCurrentWeather()
                                    viewModel.getCurrentForecast()
                                } else {
                                    //user has no saved coordinates. oblige him to turn on Location
                                    createLocationRequest()
                                }
                            }
                        }
                    }
                }
                launch {
                    viewModel.forecastState.collect { uiState ->
                        when (uiState) {
                            is DashboardViewModel.UIEventForecast.OnLoading -> {
                                if (uiState.onLoading)
                                    showLoading()
                            }
                            is DashboardViewModel.UIEventForecast.ShowErrorDialog -> {
                                hideLoadingDialog()

                                showErrorDialog({ }, uiState.resourceFailure, true)
                            }
                            is DashboardViewModel.UIEventForecast.OnCurrentForecastRetrieved -> {
                                hideLoading()

                                if (isAdded && isVisible) {
                                    forecastAdapter.setForecastList(uiState.forecast.list)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setLayoutListeners() {

        with(binding) {

            ivFavorites.setOnClickListener {
                if(findNavController().currentDestination!!.id == R.id.dashboardFragment)
                    findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToFavoritesFragment())
            }

            ivSettings.setOnClickListener {
                if(findNavController().currentDestination!!.id == R.id.dashboardFragment)
                    findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToSettingsFragment())

            }

            tvSearch.setOnClickListener {
                if(findNavController().currentDestination!!.id == R.id.dashboardFragment)
                    findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToSearchFragment())

            }
        }

    }


    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }

    private val requestCheckSettings =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    startLocationUpdates()
                }
                else ->{
                    viewModel.getLastUserCoordinates()
                }
            }
        }

    private fun initLocationCallback() {

        locationCallback =
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (locationResult.equals(null) ) {
                        return
                    }

                    for (location in locationResult.locations) {
                        if (activity != null && isAdded) {
                            viewModel.setUserCoord(Coord(location.latitude, location.longitude))
                            viewModel.getCurrentWeather()
                            viewModel.getCurrentForecast()
                        }
                    }

                    stopLocationUpdates()
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        initLocationCallback()

      /*  val task: Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(builder.build())
        task.addOnSuccessListener {*/
            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())
       // }

      /*  task.addOnFailureListener {

            Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}