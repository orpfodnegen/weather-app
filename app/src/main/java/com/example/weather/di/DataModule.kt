package com.example.weather.di

import com.example.weather.data.LocationDataSourceImpl
import com.example.weather.data.base.LocationDataSource
import com.example.weather.data.base.LocationRepository
import com.example.weather.data.base.WeatherDataSource
import com.example.weather.data.base.WeatherRepository
import com.example.weather.data.network.WeatherNetworkDataSource
import com.example.weather.data.repository.LocationRepositoryImpl
import com.example.weather.data.repository.WeatherRepositoryImpl
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
    abstract fun bindWeatherRemoteDataSource(impl: WeatherNetworkDataSource): WeatherDataSource

    @Singleton
    @Binds
    abstract fun bindLocationDataSource(impl: LocationDataSourceImpl): LocationDataSource
}
