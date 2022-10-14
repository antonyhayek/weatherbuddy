package com.antonyhayek.weatherbuddy.presentation.ui.favorites

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antonyhayek.weatherbuddy.R
import com.antonyhayek.weatherbuddy.data.local.FavoriteCity
import com.antonyhayek.weatherbuddy.databinding.FragmentFavoritesBinding
import com.antonyhayek.weatherbuddy.presentation.base.BaseFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.opencsv.CSVReader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import java.io.*


@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {
    private var favCities: List<FavoriteCity> = arrayListOf()
    private lateinit var favCitiesAdapter: FavCitiesAdapter
    private val viewModel: FavoritesViewModel by viewModels()
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutListeners()
        setupFavCitiesAdapter()
        collectFavoriteCities()
    }

    private fun setupFavCitiesAdapter() {

        favCitiesAdapter = FavCitiesAdapter(
            onFavCityClick = { position, city ->
                onCityClicked(city)
            },
            onRemoveFavClicked = { position, city ->
                onRemoveFav(city)
            }
        )

        binding.rvFavorites.adapter = favCitiesAdapter
    }

    private fun onCityClicked(city: FavoriteCity) {
        if (findNavController().currentDestination!!.id == R.id.favoritesFragment)
            findNavController().navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToWeatherDetailsFragment(
                    city.lat.toFloat(),
                    city.lon.toFloat()
                )
            )
    }

    private fun onRemoveFav(city: FavoriteCity) {
        viewModel.removeFavCity(city.id)
    }

    private fun collectFavoriteCities() {

        job = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            lifecycleScope.launchWhenCreated {
                viewModel.favoritesState.collect { uiState ->
                    when (uiState) {
                        is FavoritesViewModel.UIEventFavorites.OnLoading -> {
                            if (uiState.onLoading)
                                showLoading()
                        }
                        is FavoritesViewModel.UIEventFavorites.OnFavoriteCitiesRetrieved -> {
                            hideLoading()
                            favCities = uiState.favoriteCities
                            favCitiesAdapter.setFavCities(uiState.favoriteCities)
                        }
                        is FavoritesViewModel.UIEventFavorites.ShowErrorDialog -> {
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

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }

            fabImportCities.setOnClickListener {
                //TODO Import cities and save in room
                requestStoragePermission(true)
            }

            fabExportCities.setOnClickListener {
                //TODO Fetch Cities from room and download it to CSV
                if(favCities.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please add cities to favorites for export",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    requestStoragePermission(false)
                }

            }
        }
    }

    private fun requestStoragePermission(import: Boolean) {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ) != PackageManager.PERMISSION_GRANTED

                || ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                Dexter.withContext(requireContext())
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {

                               if(import) {
                                   selectCSV()
                               } else {
                                   exportCSV()
                               }

                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest>,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    })
                    .withErrorListener {

                    }
                    .onSameThread()
                    .check()
            } else {
                if(import) {
                    selectCSV()
                } else {
                    exportCSV()
                }
            }

    }

    private fun exportCSV() {
        var csvString = "name,lon,lat,country\n"
        for(city in favCities) {
            csvString += city.name + "," + city.lat + "," + city.lon + "," + city.country + "\n"
        }

        try {
            val root = File(Environment.getExternalStorageDirectory(), "cities_csv")
            if (!root.exists()) {
                root.mkdirs()
            }
            val gpxfile = File(root, "Cities.csv")
            val writer = FileWriter(gpxfile)
            writer.append(csvString)
            writer.flush()
            writer.close()
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private var startActivityIntent: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                importCsvFile(result.data!!.data)
            }
            else -> {
                Toast.makeText(
                    requireContext(),
                    "Please select a CSV File to import",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun importCsvFile(uri: Uri?) {

        val fileName = uri?.let { getFileName(it) }

        if(fileName == "cities.csv") {
            val csvReader = CSVReader(FileReader(Environment.getExternalStorageDirectory().toString() + "/" + fileName))
            val nextLine = arrayOf<String>()
            val count = 0
            val columns = StringBuilder();
            val value = StringBuilder();

            while ((nextLine == csvReader.readNext()) != null) {
                for(i in nextLine.indices) {
                    if (count == 0) {
                        if (i == nextLine.size - 2)
                            columns.append(nextLine[i]);
                        else
                            columns.append(nextLine[i]).append(",");
                    } else {
                        if (i == nextLine.size - 2)
                            value.append("'").append(nextLine[i]).append("'");
                        else
                            value.append("'").append(nextLine[i]).append("',");
                    }
                }

            }

            viewModel.importCitiesFromCSV(columns, value)
        }
    }


    private fun getFileName(uri: Uri): String? {
        val cursor: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)

        if(cursor != null) {
            if (cursor.count <= 0) {
                cursor.close()
                throw IllegalArgumentException("Can't obtain file name, cursor is empty")
            }
            cursor.moveToFirst()
            val fileName: String =
                cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            cursor.close()
            return fileName
        }
        return null
    }

    private fun selectCSV() {
        var intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*" //text/csv
        }
       // intent.addCategory(Intent.CATEGORY_OPENABLE)

        startActivityIntent.launch(intent)
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }

}