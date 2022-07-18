package com.example.weather.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weather.BuildConfig
import com.example.weather.R
import com.example.weather.data.Result
import com.example.weather.data.remote.OneCallResponse
import com.example.weather.databinding.FragmentWeatherBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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
            requestLastLocationOrStartLocationUpdates()
        } else {
            requestForegroundPermissions()
        }

        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeather.collect { result ->

                    when (result) {
                        is Result.Success<*> -> {
                            result.data?.let {
                                it as OneCallResponse
                                binding.temperatureText.text = it.current.feels_like.toString()
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
     * Location operations.
     *
     * Requests the last location of this device,
     * if known, otherwise start periodic location updates.
     */
    private fun requestLastLocationOrStartLocationUpdates() {
        val fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null)
                viewModel.onLocationUpdated(location)
            else
                fusedLocationClient.requestLocationUpdates(
                    getLocationRequest(),
                    getLocationCallback(),
                    Looper.getMainLooper()
                )
        }
    }

    /**
     * Method checks if permissions approved.
     */
    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    /**
     * Method requests permissions.
     */
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
                    // Request permission
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    /**
     * Handles permission result.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    requestLastLocationOrStartLocationUpdates()

                else -> {
                    // Permission denied.
                    Snackbar.make(
                        binding.fragmentWeather,
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    ).setAction(R.string.settings) {
                        // Build intent that displays the App settings screen.
                        val intent = Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }.show()
                }
            }
        }
    }

    /**
     * Sets up the location request.
     *
     * @return The LocationRequest object containing the desired parameters.
     */
    private fun getLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
    }

    /**
     * The callback that is triggered when the
     * FusedLocationClient updates the device's location.
     */
    private fun getLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                val location = locationResult.lastLocation ?: return
                viewModel.onLocationUpdated(location)
            }
        }
    }

    companion object {
        private const val TAG = "WeatherFragment"
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
    }
}
