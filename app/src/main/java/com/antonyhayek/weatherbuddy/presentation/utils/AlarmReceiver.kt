package com.antonyhayek.weatherbuddy.presentation.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.antonyhayek.weatherbuddy.MainActivity
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.data.networking.ApiService
import com.antonyhayek.weatherbuddy.data.remote.Coord
import com.antonyhayek.weatherbuddy.data.remote.LocationWeather
import com.antonyhayek.weatherbuddy.domain.database.dao.WeatherDao
import com.antonyhayek.weatherbuddy.domain.prefstore.PrefsStoreImpl
import com.antonyhayek.weatherbuddy.utils.NetworkUtils
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "1"

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var prefsStoreImpl: PrefsStoreImpl
    @Inject
    lateinit var weatherDao: WeatherDao

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var weatherResult: LocationWeather


    override fun onReceive(context: Context, intent: Intent) {
        val coordinates = runBlocking {
            Coord(prefsStoreImpl.getLastUserLat().first(), prefsStoreImpl.getLastUserLon().first())
        }

        scope.launch {
            weatherResult = if(NetworkUtils.isInternetAvailable(context)) {
                apiService.currentWeatherData(coordinates.lat, coordinates.lon)
            } else {
                weatherDao.getLocalWeather().first()
            }

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.sendReminderNotification(
                context.applicationContext,
                channelId = CHANNEL_ID,
                weatherResult = weatherResult
            )
        }

        WeatherReminderManager(context).startReminder()
    }
}

fun NotificationManager.sendReminderNotification(
    applicationContext: Context,
    channelId: String,
    weatherResult: LocationWeather
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        1,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
    )

    if (Build.VERSION.SDK_INT >= 26) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID, "weather buddy channel id",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = "weather buddy Notification Channel"
        this.createNotificationChannel(notificationChannel)
    }

    val imageName = weatherResult.weather[0].icon
    val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setContentTitle("Today\'s temperature")
        .setContentText("Today\'s temperature is " + weatherResult.main.temp.toInt().toString() + "°")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Today\'s temperature is " + weatherResult.main.temp.toInt().toString() + "°")
        )
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    val futureTarget = Glide.with(applicationContext)
        .asBitmap()
        .load("http://openweathermap.org/img/wn//$imageName@2x.png")
        .submit()

    val bitmap = futureTarget.get()
    builder.setLargeIcon(bitmap)

    Glide.with(applicationContext).clear(futureTarget)

    notify(NOTIFICATION_ID, builder.build())
}

