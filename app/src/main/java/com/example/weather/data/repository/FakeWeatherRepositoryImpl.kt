package com.example.weather.data.repository

import com.example.weather.data.base.WeatherRepository
import com.example.weather.model.CurrentWeather
import com.example.weather.model.Result
import com.example.weather.model.network.ForecastsApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeWeatherRepositoryImpl @Inject constructor() : WeatherRepository {

    override fun getCurrentWeather(lat: Double, lon: Double): Flow<Result<CurrentWeather>> {
        return flow {
            val result = CurrentWeather(
                city = "Soc Trang",
                temp = "30.0",
                description = "Cool"
            )

            emit(Result.Success(result))
        }
    }

    override fun getForecasts(lat: Double, lon: Double): Flow<Result<ForecastsApiModel>> {
        TODO("Not yet implemented")
    }
}
