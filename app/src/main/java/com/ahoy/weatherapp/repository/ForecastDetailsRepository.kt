package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.api.ApiResponse
import com.ahoy.weatherapp.api.Result
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.ForecastDetails
import com.ahoy.weatherapp.network.NetworkBoundResource
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
    val saveForecastDetailsRepository: SaveForecastDetailsRepository,
    val forecastDetailsOfflineRepository: ForecastDetailsOfflineRepository
) {
    private val controlledRunner = ControlledRunner<Flow<Result<ForecastDetails>>>()

//    suspend fun fetchForecastDetails(
//        key: String,
//        query: String,
//        days: Int,
//    ): Flow<Result<ForecastDetails>> {
//        return controlledRunner.cancelPreviousThenRun {
//            object : NetworkResource<ForecastDetails>(dispatcher) {
//                override suspend fun createCall() = apiService.fetchForecastDetails(
//                    key,
//                    query,
//                    days
//                )
//            }.asFlow()
//        }
//    }

    suspend fun fetchForecastDetails(
        key: String,
        query: String,
        days: Int,
    ): Flow<Result<ForecastDetails>> {
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkBoundResource<ForecastDetails, ForecastDetails>(dispatcher) {
                override suspend fun saveCallResult(data: ForecastDetails) {
                    saveForecastDetailsRepository.saveForecastDetails(data)
                }

                override fun shouldFetch(data: ForecastDetails?): Boolean {
                    return true
                }

                override suspend fun loadFromDb(): Flow<ForecastDetails?> {
                    return forecastDetailsOfflineRepository.fetchForecastDetails()
                }

                override fun createCall(): Flow<ApiResponse<ForecastDetails>> {
                    return apiService.fetchForecastDetails(
                        key,
                        query,
                        days
                    )
                }
            }.asFlow()
        }
    }
}
