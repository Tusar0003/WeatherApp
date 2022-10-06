package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.api.Result
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.CurrentWeatherDetails
import com.ahoy.weatherapp.network.NetworkResource
import com.ahoy.weatherapp.service.ApiService
import com.ahoy.weatherapp.utils.ControlledRunner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class WeatherDetailsRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    val apiService: ApiService,
) {
    private val controlledRunner = ControlledRunner<Flow<Result<CurrentWeatherDetails>>>()

    suspend fun fetchWeatherDetails(
        key: String,
        query: String
    ): Flow<Result<CurrentWeatherDetails>> {
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkResource<CurrentWeatherDetails>(dispatcher) {
                override suspend fun createCall() = apiService.fetchWeatherDetails(
                    key,
                    query
                )
            }.asFlow()
        }
    }
}
