package com.example.weather.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("onecall")
    suspend fun getWeatherAndForecasts(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "minutely, alerts",
        @Query("units") units: String = "metric"
    ): Response<OneCallResponse>
}
