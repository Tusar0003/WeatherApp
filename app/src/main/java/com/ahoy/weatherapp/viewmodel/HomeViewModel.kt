package com.ahoy.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahoy.weatherapp.api.Result
import com.ahoy.weatherapp.api.Status
import com.ahoy.weatherapp.domain.LoadForecastDetailsUseCase
import com.ahoy.weatherapp.domain.LoadWeatherDetailsUseCase
import com.ahoy.weatherapp.model.CurrentWeatherDetails
import com.ahoy.weatherapp.model.ForecastDetailsPayload
import com.ahoy.weatherapp.utils.Constants
import com.ahoy.weatherapp.utils.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadWeatherDetailsUseCase: LoadWeatherDetailsUseCase,
    private val loadForecastDetailsUseCase: LoadForecastDetailsUseCase
) : ViewModel() {

    private val _message = Channel<String>(Channel.CONFLATED)
    val message = _message.receiveAsFlow()

    private val currentLatLng = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val forecastDetailsPayload = MutableSharedFlow<ForecastDetailsPayload>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val weatherDetailsResponse = currentLatLng.flatMapLatest {
        loadWeatherDetailsUseCase(it)
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = Result.nothing()
    )

    val forecastDetailsResponse = forecastDetailsPayload.flatMapLatest {
        loadForecastDetailsUseCase(it)
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = Result.nothing()
    )

    var weatherDetails = MutableStateFlow<CurrentWeatherDetails>(
        CurrentWeatherDetails()
    )

    init {
        viewModelScope.launch {
            currentLatLng.collect {
                if (it.isNotEmpty()) {
                    forecastDetailsPayload.tryEmit(
                        ForecastDetailsPayload(
                            query = it,
                            days = Constants.FORECAST_DAYS
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            weatherDetailsResponse.collect{
                if (it.status == Status.SUCCESS) {
                    weatherDetails.tryEmit(it.data!!)
                } else if (it.status == Status.ERROR) {
                    _message.trySend(it.message.toString())
                }
            }
        }
    }

    fun setCurrentLatLng(latLng: String) {
        if (currentLatLng.replayCache.isEmpty()) {
            currentLatLng.tryEmit(latLng)
        }
    }
}
