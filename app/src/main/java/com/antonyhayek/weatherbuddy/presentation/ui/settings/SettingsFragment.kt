package com.antonyhayek.weatherbuddy.presentation.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antonyhayek.weatherbuddy.databinding.FragmentSettingsBinding
import com.antonyhayek.weatherbuddy.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val viewModel: SettingsViewModel by viewModels()
    private var job: Job? = null
    private val temperatureUnitsMap = hashMapOf<String, String>(
        "Celsius" to "metric",
        "Fahrenheit" to "imperial"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutListeners()
        setupUnitsSpinner()
        collectAppUnit()
    }

    private fun collectAppUnit() {

        job = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            lifecycleScope.launchWhenCreated {
                viewModel.settingsState.collect { uiState ->
                    when (uiState) {
                        is SettingsViewModel.UIEventSettings.OnLoading -> {
                            if (uiState.onLoading)
                                showLoading()
                        }
                        is SettingsViewModel.UIEventSettings.OnAppUnitRetrieved -> {
                            hideLoading()

                            binding.spinnerUnits.setSelection(
                                if (uiState.unit == "metric")
                                    0
                                else 1
                            )
                        }
                        is SettingsViewModel.UIEventSettings.ShowErrorDialog -> {
                            hideLoadingDialog()

                            showErrorDialog({ }, uiState.resourceFailure, true)
                        }

                    }
                }
            }
        }
    }

    private fun setLayoutListeners() {

        with(binding) {
            spinnerUnits.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // use position to know the selected item

                    temperatureUnitsMap[
                            binding.spinnerUnits.selectedItem.toString()
                    ]?.let {
                        viewModel.setAppUnit(
                            it
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupUnitsSpinner() {

        val unitsArrayAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                temperatureUnitsMap.keys.toTypedArray()
            )
        unitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerUnits.adapter = unitsArrayAdapter
    }

}