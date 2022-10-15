package com.antonyhayek.weatherbuddy.presentation.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

@RunWith(AndroidJUnit4::class)
class DashboardViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var dashboardViewModel: DashboardViewModel

    @Before
    fun setupViewModel() {
        dashboardViewModel = DashboardViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun getCurrentWeatherState() {
    }

    @Test
    fun setCurrentWeatherState() {
    }

    @Test
    fun getForecastState() {
    }

    @Test
    fun setForecastState() {
    }

    @Test
    fun getCurrentWeather() {
    }

    @Test
    fun getCurrentForecast() {
    }

    @Test
    fun setUserCoord() {
    }

    @Test
    fun getLastUserCoordinates() {
    }
}