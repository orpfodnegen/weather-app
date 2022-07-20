@file:Suppress("CanBeParameter")

package com.example.weather.data.repository

import com.example.weather.data.LocationDataSource
import com.example.weather.data.base.LocationRepository
import com.example.weather.di.ApplicationScope
import com.example.weather.model.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val dataSource: LocationDataSource,
    @ApplicationScope private val externalScope: CoroutineScope
) : LocationRepository {

    override val location: Flow<Location> = dataSource.locationSource.shareIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000)
    )
}
