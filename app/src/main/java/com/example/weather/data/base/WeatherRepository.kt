package com.example.weather.data.base

import com.example.weather.data.Result
import com.example.weather.data.remote.OneCallResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeatherAndForecasts(lat: Double, lon: Double): Flow<Result<OneCallResponse>>
}
