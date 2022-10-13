package com.antonyhayek.weatherbuddy.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

object JsonUtils {

    fun loadJSONFromAsset(context: Context): String {
        val json: String = try {
            val `is`: InputStream = context.assets.open("cities.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }
}