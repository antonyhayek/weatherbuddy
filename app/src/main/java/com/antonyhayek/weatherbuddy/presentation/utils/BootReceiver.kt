package com.antonyhayek.weatherbuddy.presentation.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    /*
   * restart reminders alarms when user's device reboots
   * */
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            WeatherReminderManager(context.applicationContext).startReminder()
        }
    }
}