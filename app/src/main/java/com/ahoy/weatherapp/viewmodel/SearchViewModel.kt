package com.ahoy.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahoy.weatherapp.api.Result
import com.ahoy.weatherapp.api.Status
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.domain.LoadSearchListUseCase
import com.ahoy.weatherapp.domain.SaveFavouriteCityUseCase
import com.ahoy.weatherapp.model.Search
import com.ahoy.weatherapp.utils.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val loadSearchListUseCase: LoadSearchListUseCase,
    private val saveFavouriteCityUseCase: SaveFavouriteCityUseCase,
) : ViewModel() {

    private val _message = Channel<String>(Channel.CONFLATED)
    val message = _message.receiveAsFlow()

    private val _toastMessage = Channel<String>(Channel.CONFLATED)
    val toastMessage = _toastMessage.receiveAsFlow()

    var searchText = MutableStateFlow("")

    private val searchString = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val searchListResponse = searchString.flatMapLatest {
        loadSearchListUseCase(it)
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = Result.nothing()
    )

    private val saveFavouriteCity = MutableSharedFlow<FavouriteCity>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val saveFavouriteCityResponse = saveFavouriteCity.flatMapLatest {
        saveFavouriteCityUseCase(it)
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null
    )

    var searchList = MutableStateFlow<List<Search>>(
        mutableListOf()
    )

    init {
        viewModelScope.launch {
            searchText.collect {
                delay(2000)
                if (it.isNotEmpty()) {
                    searchString.tryEmit(it)
                }
            }
        }

        viewModelScope.launch {
            searchListResponse.collect {
                if (it.status == Status.SUCCESS) {
                    if (it.data!!.isEmpty()) {
                        _toastMessage.trySend("No city found!")
                    } else {
                        searchList.tryEmit(it.data)
                    }
                } else if (it.status == Status.ERROR) {
                    _message.trySend(it.message.toString())
                }
            }
        }

        viewModelScope.launch {
            saveFavouriteCityResponse.collect {
                if (it == true) {
                    _toastMessage.trySend("City is marked and saved as favourite.")
                }
            }
        }
    }

    fun onClickFavourite(search: Search) {
        val oldList = searchList.value.toMutableList()
        val searchItem: Search = oldList.find { it.id == search.id }!!

        oldList.remove(searchItem)
        val updatedIsFavourite = !search.isFavourite
        val newSearchItem = Search(
            searchItem.id,
            searchItem.name,
            searchItem.region,
            searchItem.country,
            searchItem.lat,
            searchItem.lon,
            searchItem.url,
            updatedIsFavourite,
        )

        val newList = oldList + newSearchItem

        searchList.tryEmit(newList)
        saveFavouriteCity(newSearchItem)
    }

    private fun saveFavouriteCity(newSearchItem: Search) {
        viewModelScope.launch {
            saveFavouriteCity.tryEmit(
                FavouriteCity(
                    id = newSearchItem.id!!,
                    city = newSearchItem.name!!,
                    country = newSearchItem.country!!,
                    lat = newSearchItem.lat!!,
                    lon = newSearchItem.lon!!,
                )
            )
        }
    }
}
