package com.example.weather.data.base

import com.example.weather.data.Result
import com.example.weather.data.remote.OneCallResponse

interface WeatherDataSource {

    suspend fun fetchWeatherAndForecasts(lat: Double, lon: Double): Result<OneCallResponse>
}
