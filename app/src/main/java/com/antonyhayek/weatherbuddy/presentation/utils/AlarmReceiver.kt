package com.antonyhayek.weatherbuddy.presentation.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.antonyhayek.weatherbuddy.MainActivity
import com.antonyhayek.weatherbuddy.R

const val NOTIFICATION_ID = 1

class AlarmReceiver : BroadcastReceiver() {

    /**
     * sends notification when receives alarm
     * and then reschedule the reminder again
     * */
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendReminderNotification(
            applicationContext = context,
            channelId = "weather_buddy_channel_id"
        )
        // Remove this line if you don't want to reschedule the reminder
        WeatherReminderManager(context).startReminder()

        val intent = Intent(context, WeatherNotificationService::class.java)
        context.startService(intent)
    }
}

fun NotificationManager.sendReminderNotification(
    applicationContext: Context,
    channelId: String,
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        1,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
    )
    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setContentTitle("TEST")
        .setContentText("TEST CONTENT")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("DESCRIPTION")
        )
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

