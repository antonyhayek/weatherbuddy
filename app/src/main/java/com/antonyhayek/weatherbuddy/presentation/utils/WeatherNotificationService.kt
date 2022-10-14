package com.antonyhayek.weatherbuddy.presentation.utils

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.content.Context
import android.os.IBinder
import androidx.core.app.JobIntentService
import com.antonyhayek.weatherbuddy.data.networking.ApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WeatherNotificationService @Inject constructor(
    private var apiService: ApiService
) : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {



        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}