package com.ahoy.weatherapp.domain

import com.ahoy.weatherapp.api.FlowUseCase
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.Search
import com.ahoy.weatherapp.repository.SearchRepository
import com.ahoy.weatherapp.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
open class LoadSearchListUseCase @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: SearchRepository
) : FlowUseCase<String, List<Search>>(ioDispatcher) {

    override suspend fun execute(parameters: String) = repository.fetchSearchList(
        Constants.API_KEY,
        parameters
    )
}
