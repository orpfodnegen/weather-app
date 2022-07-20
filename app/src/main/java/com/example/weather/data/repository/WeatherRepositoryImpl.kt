package com.example.weather.data.repository

import com.example.weather.data.base.WeatherDataSource
import com.example.weather.data.base.WeatherRepository
import com.example.weather.di.IoDispatcher
import com.example.weather.model.Result
import com.example.weather.model.network.CurrentWeatherApiModel
import com.example.weather.model.network.ForecastsApiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WeatherRepository {

    override fun getCurrentWeather(lat: Double, lon: Double)
        : Flow<Result<CurrentWeatherApiModel>> =
        flow {
            val result = remoteDataSource.fetchCurrentWeather(lat, lon)
            emit(result)
        }.flowOn(ioDispatcher)

    override fun getForecasts(lat: Double, lon: Double): Flow<Result<ForecastsApiModel>> {
        TODO("Not yet implemented")
    }
}