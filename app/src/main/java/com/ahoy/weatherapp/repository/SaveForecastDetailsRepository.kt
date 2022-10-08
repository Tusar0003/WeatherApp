package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.db.WeatherAppDatabase
import com.ahoy.weatherapp.db.entity.ForecastDetailsOffline
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.ForecastDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class SaveForecastDetailsRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val database: WeatherAppDatabase,
) {
    suspend fun saveForecastDetails(forecastDetails: ForecastDetails): Flow<Boolean> {
        val isInserted = MutableStateFlow<Boolean>(false)
        forecastDetails.forecast?.forecastday?.forEach {
            database.forecastDetailsDao.insert(
                ForecastDetailsOffline(
                    date = it.date!!,
                    averageTemperature = it.day?.avgtempC!!,
                    icon = it.day?.condition?.icon!!,
                )
            )
        }
        isInserted.tryEmit(true)
        return isInserted
    }
}
