package com.antonyhayek.weatherbuddy.presentation.ui.favorites

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.data.local.City
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.databinding.ItemFavCityBinding
import kotlin.collections.ArrayList

class FavCitiesAdapter(
    private var onFavCityClick: (position: Int, city: FavoriteCity) -> Unit,
    private var onRemoveFavClicked: (position: Int, city: FavoriteCity) -> Unit,
    ) : RecyclerView.Adapter<FavCitiesAdapter.CitiesAdapterViewHolder>() {
    private lateinit var binding: ItemFavCityBinding
    private lateinit var context: Context
    private var favCityList: ArrayList<FavoriteCity> = ArrayList()

    fun setFavCities(_citiesList: List<FavoriteCity>) {
        favCityList.clear()
        favCityList.addAll(_citiesList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CitiesAdapterViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemFavCityBinding.inflate(inflater, parent, false)
        return CitiesAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitiesAdapterViewHolder, position: Int) {
        val city = favCityList[position]
        holder.bind(city/*, isSelectionEnabled,*/, context)

        holder.itemView.setOnClickListener{
            onFavCityClick(position, city)
        }

        holder.binding.ivRemoveFav.setOnClickListener {
            onRemoveFavClicked(position, city)
        }
    }

    override fun getItemCount(): Int = favCityList.size

    class CitiesAdapterViewHolder(val binding: ItemFavCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            city: FavoriteCity,
           // isSelectionEnabled: Boolean,
            context: Context,
        ) {
            binding.city = city

            if(city.isFavCity) {
                ImageViewCompat.setImageTintList(binding.ivRemoveFav, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.purple_500))
                )

            } else {
                ImageViewCompat.setImageTintList(binding.ivRemoveFav, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.black))
                )

            }
        }
    }


}
