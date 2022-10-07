package com.ahoy.weatherapp.repository

import com.ahoy.weatherapp.api.Result
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.Search
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
class SearchRepository @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    val apiService: ApiService,
) {
    private val controlledRunner = ControlledRunner<Flow<Result<List<Search>>>>()

    suspend fun fetchSearchList(
        key: String,
        query: String
    ): Flow<Result<List<Search>>> {
        return controlledRunner.cancelPreviousThenRun {
            object : NetworkResource<List<Search>>(dispatcher) {
                override suspend fun createCall() = apiService.fetchSearchList(
                    key,
                    query
                )
            }.asFlow()
        }
    }
}
