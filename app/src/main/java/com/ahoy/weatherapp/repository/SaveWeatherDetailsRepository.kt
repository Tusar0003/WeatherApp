package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.db.WeatherAppDatabase
import com.ahoy.weatherapp.db.entity.CurrentWeatherDetailsOffline
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.CurrentWeatherDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class SaveWeatherDetailsRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val database: WeatherAppDatabase,
) {
    suspend fun saveWeatherDetails(currentWeatherDetails: CurrentWeatherDetails): Flow<Boolean> {
        val isInserted = MutableStateFlow<Boolean>(false)
        database.weatherDetailsDao.insert(
            CurrentWeatherDetailsOffline(
                city = currentWeatherDetails.location?.name!!,
                status = currentWeatherDetails.current?.condition?.text!!,
                currentTemperature = currentWeatherDetails.current?.tempC!!,
                feelsLike = currentWeatherDetails.current?.feelslikeC!!,
                wind = currentWeatherDetails.current?.windKph!!,
                humidity = currentWeatherDetails.current?.humidity!!,
                icon = currentWeatherDetails.current?.condition?.icon!!,
            )
        )
        isInserted.tryEmit(true)
        return isInserted
    }
}
