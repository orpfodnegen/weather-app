package com.example.weather.data.base

import com.example.weather.model.OneCallResponse
import com.example.weather.model.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeatherAndForecasts(lat: Double, lon: Double): Flow<Result<OneCallResponse>>
}
