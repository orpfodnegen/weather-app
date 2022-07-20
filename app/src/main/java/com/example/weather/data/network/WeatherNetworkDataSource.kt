package com.example.weather.data.network

import com.example.weather.data.base.WeatherDataSource
import com.example.weather.di.IoDispatcher
import com.example.weather.model.Result
import com.example.weather.model.network.CurrentWeatherApiModel
import com.example.weather.model.network.ForecastsApiModel
import com.example.weather.util.ErrorUtils
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherNetworkDataSource @Inject constructor(
    private val retrofit: Retrofit,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WeatherDataSource {

    private val apiService = retrofit.create(WeatherApiService::class.java)

    override suspend fun fetchCurrentWeather(lat: Double, lon: Double)
        : Result<CurrentWeatherApiModel> =
        getResponse { apiService.fetchCurrentWeather(lat, lon) }

    override suspend fun fetchForecast(lat: Double, lon: Double): Result<ForecastsApiModel> {
        TODO("Not yet implemented")
    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>)
        : Result<T> {
        return try {
            val result = request()

            if (result.isSuccessful)
                Result.Success(result.body())
            else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                return errorResponse!!
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
