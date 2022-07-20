package com.example.weather.data.network

import com.example.weather.model.network.CurrentWeatherApiModel
import com.example.weather.model.network.ForecastsApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun fetchCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): Response<CurrentWeatherApiModel>

    @GET("onecall")
    suspend fun fetchForecasts(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "current,minutely,alerts",
        @Query("units") units: String = "metric"
    ): Response<ForecastsApiModel>
}
