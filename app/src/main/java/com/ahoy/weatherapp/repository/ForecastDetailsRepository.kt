package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.api.Result
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.ForecastDetails
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
class ForecastDetailsRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    val apiService: ApiService,
) {
    private val controlledRunner = ControlledRunner<Flow<Result<ForecastDetails>>>()

    suspend fun fetchForecastDetails(
        key: String,
        query: String,
        days: Int,
    ): Flow<Result<ForecastDetails>> {
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkResource<ForecastDetails>(dispatcher) {
                override suspend fun createCall() = apiService.fetchForecastDetails(
                    key,
                    query,
                    days
                )
            }.asFlow()
        }
    }
}
