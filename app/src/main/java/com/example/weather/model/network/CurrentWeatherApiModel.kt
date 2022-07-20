package com.example.weather.model.network

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherApiModel(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherItem>,
    val wind: Wind
)

@Serializable
data class Clouds(
    val all: Int
)

@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)

@Serializable
data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

@Serializable
data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int
)

@Serializable
data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)
