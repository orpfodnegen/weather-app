package com.example.weather.data.base

import com.example.weather.model.CurrentWeather
import com.example.weather.model.Result
import com.example.weather.model.network.ForecastsApiModel
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getCurrentWeather(lat: Double, lon: Double): Flow<Result<CurrentWeather>>

    fun getForecasts(lat: Double, lon: Double): Flow<Result<ForecastsApiModel>>
}
