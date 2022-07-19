package com.example.weather.data.remote

import android.util.Log
import com.example.weather.data.base.WeatherDataSource
import com.example.weather.di.IoDispatcher
import com.example.weather.model.OneCallResponse
import com.example.weather.model.Result
import com.example.weather.util.ErrorUtils
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

private const val TAG = "WeatherRemoteDataSource"

class WeatherRemoteDataSource @Inject constructor(
    private val retrofit: Retrofit,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WeatherDataSource {

    private val apiService = retrofit.create(WeatherApiService::class.java)

    override suspend fun fetchWeatherAndForecasts(
        lat: Double,
        lon: Double,
    ): Result<OneCallResponse> {
        return getResponse { apiService.getWeatherAndForecasts(lat, lon) }
    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>): Result<T> {
        return try {
            Log.d(TAG, "I'm working in thread ${Thread.currentThread().name}")
            val result = request()

            if (result.isSuccessful) {
                Result.Success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                return errorResponse!!
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
