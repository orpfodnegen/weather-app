package com.example.weather.model

import android.location.Location

data class MapLocation(val latitude: Double = 0.0, val longitude: Double = 0.0)

fun Location.asModel(): MapLocation {
    return MapLocation(latitude, longitude)
}
