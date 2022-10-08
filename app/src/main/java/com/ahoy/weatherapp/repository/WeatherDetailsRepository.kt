package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.api.ApiResponse
import com.ahoy.weatherapp.api.Result
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.Condition
import com.ahoy.weatherapp.model.Current
import com.ahoy.weatherapp.model.CurrentWeatherDetails
import com.ahoy.weatherapp.model.Location
import com.ahoy.weatherapp.network.NetworkBoundResource
import com.ahoy.weatherapp.service.ApiService
import com.ahoy.weatherapp.utils.ControlledRunner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class WeatherDetailsRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    val apiService: ApiService,
    val saveWeatherDetailsRepository: SaveWeatherDetailsRepository,
    val weatherDetailsOfflineRepository: WeatherDetailsOfflineRepository
) {
    private val controlledRunner = ControlledRunner<Flow<Result<CurrentWeatherDetails>>>()

//    suspend fun fetchWeatherDetails(
//        key: String,
//        query: String
//    ): Flow<Result<CurrentWeatherDetails>> {
//        return controlledRunner.cancelPreviousThenRun {
//            object : NetworkResource<CurrentWeatherDetails>(dispatcher) {
//                override suspend fun createCall() = apiService.fetchWeatherDetails(
//                    key,
//                    query
//                )
//            }.asFlow()
//        }
//    }

    suspend fun fetchWeatherDetails(
        key: String,
        query: String
    ): Flow<Result<CurrentWeatherDetails>> {
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkBoundResource<CurrentWeatherDetails, CurrentWeatherDetails>(dispatcher) {
                override fun shouldFetch(data: CurrentWeatherDetails?): Boolean {
                    return true
                }

                override suspend fun loadFromDb(): Flow<CurrentWeatherDetails?> {
                    val weatherDetailsOffline = weatherDetailsOfflineRepository.fetchWeatherDetails()
                    return flow {
                        emit(
                            CurrentWeatherDetails(
                                location = Location(
                                    name = weatherDetailsOffline.first()?.city,
                                ),
                                current = Current(
                                    tempC = weatherDetailsOffline.first()?.currentTemperature,
                                    feelslikeC = weatherDetailsOffline.first()?.feelsLike,
                                    windKph = weatherDetailsOffline.first()?.wind,
                                    humidity = weatherDetailsOffline.first()?.humidity,
                                    condition = Condition(
                                        text = weatherDetailsOffline.first()?.status,
                                        icon = weatherDetailsOffline.first()?.icon
                                    )
                                )
                            )
                        )
                    }
                }

                override suspend fun saveCallResult(data: CurrentWeatherDetails) {
                    saveWeatherDetailsRepository.saveWeatherDetails(data)
                }

                override fun createCall(): Flow<ApiResponse<CurrentWeatherDetails>> {
                    return apiService.fetchWeatherDetails(
                        key,
                        query
                    )
                }
            }.asFlow()
        }
    }
}
