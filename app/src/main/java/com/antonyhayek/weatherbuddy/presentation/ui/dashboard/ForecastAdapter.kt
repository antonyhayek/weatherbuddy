package com.antonyhayek.weatherbuddy.presentation.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antonyhayek.weatherbuddy.data.remote.ForecastMain
import com.antonyhayek.weatherbuddy.databinding.ItemForecastBinding
import com.antonyhayek.weatherbuddy.utils.ImageUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ForecastAdapter() : RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>() {
    private lateinit var binding: ItemForecastBinding
    private lateinit var context: Context
    private var forecastList: ArrayList<ForecastMain> = ArrayList()

    fun setForecastList(_forecastList: List<ForecastMain>) {
        forecastList.clear()
        forecastList.addAll(_forecastList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastAdapterViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemForecastBinding.inflate(inflater, parent, false)
        return ForecastAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastAdapterViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bind(forecast/*, isSelectionEnabled,*/, context)

    }

    override fun getItemCount(): Int = forecastList.size

    class ForecastAdapterViewHolder(val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            forecast: ForecastMain,
           // isSelectionEnabled: Boolean,
            context: Context,
        ) {

            with(binding) {
                val calendar = Calendar.getInstance()
          //     calendar.time = forecast.dt_txt
              /*  calendar.timeInMillis = forecast.dt
                val date: DateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                tvDay.text = date.format(Calendar.getInstance().time)*/

                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(forecast.dt_txt)
                calendar.time = date!!
                val day: DateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                tvDay.text = day.format(calendar.time)

                tvCondition.text = forecast.weather[0].description
                tvTemperature.text = forecast.main.temp_min.toInt().toString() + "\u00B0/" + forecast.main.temp_max.toInt().toString() + "\u00B0"

                ImageUtils.downloadImage(
                    context,
                    forecast.weather[0].icon,
                    binding.ivWeatherIcon,
                    0
                )
            }

        }
    }


}
