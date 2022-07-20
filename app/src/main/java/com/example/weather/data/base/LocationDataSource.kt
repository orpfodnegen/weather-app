package com.example.weather.data.base

import com.example.weather.model.Location
import com.google.android.gms.location.LocationRequest
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {

    val request: LocationRequest

    fun getLocationSource(): Flow<Location>
}
