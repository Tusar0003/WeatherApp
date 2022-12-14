package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.db.WeatherAppDatabase
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class SaveFavouriteCityRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val database: WeatherAppDatabase,
) {
    suspend fun saveFavouriteCity(favouriteCity: FavouriteCity): Flow<Boolean> {
        val isInserted = MutableStateFlow<Boolean>(false)
        database.favouriteCityDao.insert(favouriteCity)
        isInserted.tryEmit(true)
        return isInserted
    }
}
