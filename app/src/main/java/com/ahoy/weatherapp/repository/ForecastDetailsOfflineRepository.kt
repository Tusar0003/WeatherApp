package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.db.WeatherAppDatabase
import com.ahoy.weatherapp.db.entity.CurrentWeatherDetailsOffline
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.*
import com.ahoy.weatherapp.network.OfflineNetworkResource
import com.ahoy.weatherapp.utils.ControlledRunner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class ForecastDetailsOfflineRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val database: WeatherAppDatabase,
) {
    private val controlledRunner = ControlledRunner<Flow<ForecastDetails?>>()

    suspend fun fetchForecastDetails(): Flow<ForecastDetails?> {
        return controlledRunner.cancelPreviousThenRun {
            object : OfflineNetworkResource<ForecastDetails?>(dispatcher) {
                override suspend fun loadFromDb(): Flow<ForecastDetails?> {
                    val forecastDetailsOffline = database.forecastDetailsDao.getWeatherDetails()
                    val forecastDayList = mutableListOf<Forecastday>()
                    forecastDetailsOffline.first().forEach {
                        forecastDayList.add(
                            Forecastday(
                                date = it?.date,
                                day = Day(
                                    avgtempC = it?.averageTemperature,
                                    condition = Condition(
                                        icon = it?.icon
                                    )
                                )
                            )
                        )
                    }

                    return flow {
                        emit(
                            ForecastDetails(
                                forecast = Forecast(
                                    forecastday = forecastDayList
                                )
                            )
                        )
                    }
                }
            }.asFlow()
        }
    }
}
