package com.example.weather.data.base

import com.example.weather.model.MapLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    val location: Flow<MapLocation>
}
