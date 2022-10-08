package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.db.WeatherAppDatabase
import com.ahoy.weatherapp.db.entity.CurrentWeatherDetailsOffline
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.network.OfflineNetworkResource
import com.ahoy.weatherapp.utils.ControlledRunner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class WeatherDetailsOfflineRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val database: WeatherAppDatabase,
) {
    private val controlledRunner = ControlledRunner<Flow<CurrentWeatherDetailsOffline?>>()

    suspend fun fetchWeatherDetails(): Flow<CurrentWeatherDetailsOffline?> {
        return controlledRunner.cancelPreviousThenRun {
            object : OfflineNetworkResource<CurrentWeatherDetailsOffline?>(dispatcher) {
                override suspend fun loadFromDb(): Flow<CurrentWeatherDetailsOffline?> {
                    return database.weatherDetailsDao.getWeatherDetails()
                }
            }.asFlow()
        }
    }
}
