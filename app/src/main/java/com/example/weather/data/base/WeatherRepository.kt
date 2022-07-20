package com.example.weather.data.base

import com.example.weather.model.Result
import com.example.weather.model.network.CurrentWeatherApiModel
import com.example.weather.model.network.ForecastsApiModel
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getCurrentWeather(lat: Double, lon: Double): Flow<Result<CurrentWeatherApiModel>>

    fun getForecasts(lat: Double, lon: Double): Flow<Result<ForecastsApiModel>>
}
