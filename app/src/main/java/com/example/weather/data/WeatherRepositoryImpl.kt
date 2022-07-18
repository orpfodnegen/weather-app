package com.example.weather.data

import com.example.weather.data.base.WeatherDataSource
import com.example.weather.data.base.WeatherRepository
import com.example.weather.data.remote.OneCallResponse
import com.example.weather.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WeatherRepository {

    override fun getWeatherAndForecasts(lat: Double, lon: Double): Flow<Result<OneCallResponse>> {
        return flow {
            val result = remoteDataSource.fetchWeatherAndForecasts(lat, lon)
            emit(result)
        }.flowOn(ioDispatcher)
    }
}
