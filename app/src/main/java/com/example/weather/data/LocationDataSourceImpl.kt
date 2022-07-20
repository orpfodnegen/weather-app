package com.example.weather.data

import android.os.Looper
import com.example.weather.data.base.LocationDataSource
import com.example.weather.model.Location
import com.example.weather.util.Config
import com.example.weather.util.asModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationDataSourceImpl @Inject constructor(
    private val client: FusedLocationProviderClient
) : LocationDataSource {
    override val request = LocationRequest.create().apply {
        interval = TimeUnit.MINUTES.toMillis(updateIntervalMinutes)
        fastestInterval = TimeUnit.MINUTES.toMillis(fastestUpdateIntervalMinutes)
        priority = Priority.PRIORITY_HIGH_ACCURACY
    }

    override fun getLocationSource(): Flow<Location> {
        return callbackFlow {
            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { trySend(it.asModel()) }
                }
            }

            client.requestLocationUpdates(request, callback, Looper.getMainLooper())
                .addOnFailureListener { close(it) }
            awaitClose { client.removeLocationUpdates(callback) }
        }
    }

    companion object {
        private const val updateIntervalMinutes = Config.UPDATE_INTERVAL_MINUTES
        private const val fastestUpdateIntervalMinutes = Config.FASTEST_UPDATE_INTERVAL_MINUTES
    }
}
