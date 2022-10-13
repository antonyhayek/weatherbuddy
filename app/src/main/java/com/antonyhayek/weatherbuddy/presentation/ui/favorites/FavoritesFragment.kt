package com.antonyhayek.weatherbuddy.presentation.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.databinding.FragmentFavoritesBinding
import com.antonyhayek.weatherbuddy.presentation.base.BaseFragment


class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setLayoutListeners()
    }

    private fun setLayoutListeners() {

        with(binding) {

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}