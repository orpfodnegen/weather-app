@file:Suppress("CanBeParameter")

package com.example.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.base.LocationRepository
import com.example.weather.data.base.WeatherRepository
import com.example.weather.di.ApplicationScope
import com.example.weather.model.Result
import com.example.weather.model.network.CurrentWeatherApiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    @ApplicationScope private val externalScope: CoroutineScope
) : ViewModel() {

    private var _currentWeather = MutableStateFlow<Result<CurrentWeatherApiModel>>(Result.Loading)
    val currentWeather: StateFlow<Result<CurrentWeatherApiModel>> = _currentWeather

    fun onForegroundPermissionApproved() {
        viewModelScope.launch {
            locationRepository.location.collect { location ->
                weatherRepository.getCurrentWeather(
                    location.latitude,
                    location.longitude
                ).collect { currentWeather ->
                    _currentWeather.emit(currentWeather)
                }
            }
        }
    }
}
