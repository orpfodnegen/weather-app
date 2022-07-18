package com.example.weather.di

import com.example.weather.data.WeatherRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository
}

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRemoteDataSource(impl: WeatherRemoteDataSource): WeatherDataSource
}
