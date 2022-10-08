package com.ahoy.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahoy.weatherapp.api.Result
import com.ahoy.weatherapp.api.Status
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.domain.LoadFavouriteCityUseCase
import com.ahoy.weatherapp.domain.LoadWeatherDetailsUseCase
import com.ahoy.weatherapp.model.CurrentWeatherDetails
import com.ahoy.weatherapp.model.Search
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
class FavouriteViewModel @Inject constructor(
    private val loadFavouriteCityUseCase: LoadFavouriteCityUseCase,
    private val loadWeatherDetailsUseCase: LoadWeatherDetailsUseCase
) : ViewModel() {

    private val _message = Channel<String>(Channel.CONFLATED)
    val message = _message.receiveAsFlow()

    private val _navigationActions = Channel<FavouriteNavigationAction>(Channel.CONFLATED)
    val navigationActions = _navigationActions.receiveAsFlow()

    private val getFavouriteCity = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val currentLatLng = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val favouriteCityList = getFavouriteCity.flatMapLatest {
        loadFavouriteCityUseCase(it)
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null
    )

    val weatherDetailsResponse = currentLatLng.flatMapLatest {
        loadWeatherDetailsUseCase(it)
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
            weatherDetailsResponse.collect {
                if (it.status == Status.SUCCESS) {
                    weatherDetails.tryEmit(it.data!!)
                    _navigationActions.trySend(FavouriteNavigationAction.NavigateToDetailsAction)
                } else if (it.status == Status.ERROR) {
                    _message.trySend(it.message.toString())
                }
            }
        }
    }

    fun getFavouriteCity() {
        viewModelScope.launch {
            getFavouriteCity.tryEmit(Unit)
        }
    }

    private fun getWeatherDetails(favouriteCity: FavouriteCity) {
        viewModelScope.launch {
            currentLatLng.tryEmit(
                "${favouriteCity.lat}, ${favouriteCity.lon}"
            )
        }
    }

    fun onClickDetails(favouriteCity: FavouriteCity) {
        getWeatherDetails(favouriteCity)
    }
}

sealed class FavouriteNavigationAction {
    object NavigateToDetailsAction : FavouriteNavigationAction()
}
