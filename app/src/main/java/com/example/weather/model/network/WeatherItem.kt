package com.example.weather.model.network

import kotlinx.serialization.Serializable

@Serializable
data class WeatherItem(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)
