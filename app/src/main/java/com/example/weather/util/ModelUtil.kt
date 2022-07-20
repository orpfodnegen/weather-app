package com.example.weather.util

import com.example.weather.model.CurrentWeather
import com.example.weather.model.Location
import com.example.weather.model.network.CurrentWeatherApiModel

fun android.location.Location.asModel(): Location {
    return Location(latitude, longitude)
}

fun CurrentWeatherApiModel.asModel(): CurrentWeather = CurrentWeather(
    city = name,
    temp = main.temp.toString(),
    description = weather.first().main
)
