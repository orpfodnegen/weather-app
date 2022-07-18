@file:Suppress("CanBeParameter")

package com.example.weather.ui

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.Result
import com.example.weather.data.base.WeatherRepository
import com.example.weather.data.remote.OneCallResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _currentLocation = MutableStateFlow<Location?>(null)

    val currentWeather: StateFlow<Result<OneCallResponse>> =
        repository.getWeatherAndForecasts(33.44, -94.04)
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5000),
                initialValue = Result.Loading
            )

    /**
     * Invoked immediately when the operating
     * system gets the devices location.
     */
    fun onLocationUpdated(location: Location) {
        _currentLocation.value = location
    }
}
