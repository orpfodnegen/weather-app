package com.example.weather.model

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

fun android.location.Location.asModel(): Location {
    return Location(latitude, longitude)
}
