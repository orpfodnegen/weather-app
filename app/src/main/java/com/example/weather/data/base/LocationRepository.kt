package com.example.weather.data.base

import com.example.weather.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    val location: Flow<Location>
}
