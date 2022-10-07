package com.ahoy.weatherapp.domain

import com.ahoy.weatherapp.api.FlowUseCase
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.ForecastDetails
import com.ahoy.weatherapp.model.ForecastDetailsPayload
import com.ahoy.weatherapp.repository.ForecastDetailsRepository
import com.ahoy.weatherapp.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
open class LoadForecastDetailsUseCase @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: ForecastDetailsRepository
) : FlowUseCase<ForecastDetailsPayload, ForecastDetails>(ioDispatcher) {

    override suspend fun execute(parameters: ForecastDetailsPayload) = repository.fetchForecastDetails(
        Constants.API_KEY,
        parameters.query,
        parameters.days
    )
}
