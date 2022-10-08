package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.api.Result
import com.ahoy.weatherapp.db.WeatherAppDatabase
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.CurrentWeatherDetails
import com.ahoy.weatherapp.network.NetworkBoundResource
import com.ahoy.weatherapp.network.NetworkResource
import com.ahoy.weatherapp.utils.ControlledRunner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class FavouriteCityListRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val database: WeatherAppDatabase,
) {
    private val controlledRunner = ControlledRunner<Flow<List<FavouriteCity>?>>()

    suspend fun fetchFavouriteCityList(): Flow<List<FavouriteCity>?> {
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkBoundResource<List<FavouriteCity>?>(dispatcher) {
                override suspend fun loadFromDb(): Flow<List<FavouriteCity>?> {
                    return database.favouriteCityDao.getFavouriteCityList()
                }
            }.asFlow()
        }
    }
}
