package com.example.weather.di

import com.example.weather.data.LocationRepositoryImpl
import com.example.weather.data.WeatherRepositoryImpl
import com.example.weather.data.base.LocationRepository
import com.example.weather.data.base.WeatherDataSource
import com.example.weather.data.base.WeatherRepository
import com.example.weather.data.remote.WeatherRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Singleton
    @Binds
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository
}

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindWeatherRemoteDataSource(impl: WeatherRemoteDataSource): WeatherDataSource
}
