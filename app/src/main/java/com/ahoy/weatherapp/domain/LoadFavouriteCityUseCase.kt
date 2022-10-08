package com.ahoy.weatherapp.domain

import com.ahoy.weatherapp.api.FlowDatabaseUseCase
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.repository.FavouriteCityListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
open class LoadFavouriteCityUseCase @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: FavouriteCityListRepository
) : FlowDatabaseUseCase<Unit, List<FavouriteCity>?>(ioDispatcher) {

    override suspend fun execute(parameters: Unit) = repository.fetchFavouriteCityList()
}
