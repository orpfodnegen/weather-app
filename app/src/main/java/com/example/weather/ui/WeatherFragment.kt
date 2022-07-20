package com.example.weather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.model.CurrentWeather
import com.example.weather.model.Result
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val binding: FragmentWeatherBinding by lazy {
        FragmentWeatherBinding.inflate(layoutInflater)
    }

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (foregroundPermissionApproved()) {
            viewModel.onForegroundPermissionApproved()
            subscribeUi()
        } else {
            requestForegroundPermissions()
        }

        return binding.root
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeather.collect { result ->

                    when (result) {
                        is Result.Success<CurrentWeather> -> {
                            result.data?.let {
                                binding.temperatureText.text = it.temp
                                binding.cityText.text = it.city
                            }
                            binding.loading.visibility = View.GONE
                        }

                        is Result.Error, is Result.ResponseError -> {
                            result.toString().let {
                                Log.e(TAG, it)
                                showError(it)
                            }
                            binding.loading.visibility = View.GONE
                        }

                        is Result.Loading -> {
                            binding.loading.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun showError(msg: String) {
        Snackbar.make(binding.fragmentWeather, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("DISMISS") {
            }.show()
    }

    /**
     * Register the permissions callback, which handles the user's response to the
     * system permissions dialog.
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.onForegroundPermissionApproved()
            subscribeUi()
        } else {
            Snackbar.make(
                binding.fragmentWeather,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                binding.fragmentWeather,
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        private const val TAG = "WeatherFragment"
    }
}
