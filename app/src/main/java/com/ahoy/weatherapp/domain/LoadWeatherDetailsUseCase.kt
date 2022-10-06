package com.ahoy.weatherapp.domain

import com.ahoy.weatherapp.api.FlowUseCase
import com.ahoy.weatherapp.di.IoDispatcher
import com.ahoy.weatherapp.model.CurrentWeatherDetails
import com.ahoy.weatherapp.repository.WeatherDetailsRepository
import com.ahoy.weatherapp.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
open class LoadWeatherDetailsUseCase @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: WeatherDetailsRepository
) : FlowUseCase<String, CurrentWeatherDetails>(ioDispatcher) {

    override suspend fun execute(parameters: String) = repository.fetchWeatherDetails(
        Constants.API_KEY,
        parameters
    )
}