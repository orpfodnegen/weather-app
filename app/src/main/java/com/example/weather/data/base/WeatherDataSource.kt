package com.example.weather.data.base

import com.example.weather.model.OneCallResponse
import com.example.weather.model.Result

interface WeatherDataSource {

    suspend fun fetchWeatherAndForecasts(lat: Double, lon: Double): Result<OneCallResponse>
}
