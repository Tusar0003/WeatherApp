package com.ahoy.weatherapp.domain

import com.ahoy.weatherapp.api.FlowDatabaseUseCase
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.repository.FavouriteCityListRepository
import com.ahoy.weatherapp.repository.SaveFavouriteCityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
open class SaveFavouriteCityUseCase @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: SaveFavouriteCityRepository
) : FlowDatabaseUseCase<FavouriteCity, Boolean>(ioDispatcher) {

    override suspend fun execute(parameters: FavouriteCity): Flow<Boolean> = repository.saveFavouriteCity(parameters)
}
