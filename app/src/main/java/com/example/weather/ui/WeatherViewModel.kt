@file:Suppress("CanBeParameter")

package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.base.LocationRepository
import com.example.weather.data.base.WeatherRepository
import com.example.weather.di.ApplicationScope
import com.example.weather.model.MapLocation
import com.example.weather.model.OneCallResponse
import com.example.weather.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    @ApplicationScope private val externalScope: CoroutineScope
) : ViewModel() {

    private val _currentLocation = MutableStateFlow(MapLocation())

    val currentWeather: StateFlow<Result<OneCallResponse>> = _currentLocation.flatMapLatest {
        weatherRepository.getWeatherAndForecasts(it.latitude, it.longitude)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = Result.Loading
    )

    fun onForegroundPermissionApproved() {
        viewModelScope.launch {
            locationRepository.location.collect {
                Log.d(TAG, it.toString())
            }
        }
    }

    companion object {
        private const val TAG = "WeatherViewModel"
    }
}
