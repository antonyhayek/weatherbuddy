package com.antonyhayek.weatherbuddy.presentation.ui.citysearch

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
import com.antonyhayek.weatherbuddy.databinding.ItemCityBinding
import kotlin.collections.ArrayList

class CitiesAdapter(
    private var onCityClicked: (position: Int, city: City) -> Unit,
    private var onFavClicked: (position: Int, city: City) -> Unit,
    ) : RecyclerView.Adapter<CitiesAdapter.CitiesAdapterViewHolder>(), Filterable {
    private lateinit var binding: ItemCityBinding
    private lateinit var context: Context
    private var cityList: ArrayList<City> = ArrayList()
    private var tempCityList: ArrayList<City> = cityList
 //   private var isSelectionEnabled: Boolean = false

    fun setCities(_citiesList: List<City>) {
        cityList.clear()
        cityList.addAll(_citiesList)
        tempCityList = cityList
        notifyDataSetChanged()
    }

   /* fun setSelectionEnabled(selectionEnabled: Boolean) {
        this.isSelectionEnabled = selectionEnabled
    }
*/

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CitiesAdapterViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemCityBinding.inflate(inflater, parent, false)
        return CitiesAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitiesAdapterViewHolder, position: Int) {
        val city = tempCityList[position]
        holder.bind(city/*, isSelectionEnabled,*/, context)

        holder.itemView.setOnClickListener{
            onCityClicked(position, city)
        }

        holder.binding.ivAddToFav.setOnClickListener {
            onFavClicked(position, city)
        }
    }

    override fun getItemCount(): Int = tempCityList.size

    class CitiesAdapterViewHolder(val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            city: City,
           // isSelectionEnabled: Boolean,
            context: Context,
        ) {
            binding.city = city

            if(city.isFavCity) {
                ImageViewCompat.setImageTintList(binding.ivAddToFav, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.purple_500))
                )

            } else {
                ImageViewCompat.setImageTintList(binding.ivAddToFav, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.black))
                )

                /*binding.ivAddToFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_favorites
                    )
                )*/

            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                tempCityList = if (charSearch.isEmpty()) {
                    cityList
                } else {
                    val resultList: ArrayList<City> = arrayListOf()
                    for (city in cityList) {
                        if (city.name.lowercase()
                                .contains(charSearch.lowercase()) ||
                            city.country.lowercase()
                                .contains(charSearch.lowercase())
                        ) {
                            resultList.add(city)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = tempCityList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                tempCityList = results?.values as ArrayList<City>
                notifyDataSetChanged()
            }
        }
    }

}
