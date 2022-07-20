package com.example.weather.data.base

import com.example.weather.model.Result
import com.example.weather.model.network.CurrentWeatherApiModel
import com.example.weather.model.network.ForecastsApiModel

interface WeatherDataSource {

    suspend fun fetchCurrentWeather(lat: Double, lon: Double): Result<CurrentWeatherApiModel>

    suspend fun fetchForecast(lat: Double, lon: Double): Result<ForecastsApiModel>
}
