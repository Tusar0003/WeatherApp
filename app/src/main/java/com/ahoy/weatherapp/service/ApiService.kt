package com.ahoy.weatherapp.service

import com.ahoy.weatherapp.api.ApiResponse
import com.ahoy.weatherapp.model.CurrentWeatherDetails
import com.ahoy.weatherapp.model.ForecastDetails
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("current.json")
    fun fetchWeatherDetails(
        @Query("key") key: String,
        @Query("q") query: String,
    ) : Flow<ApiResponse<CurrentWeatherDetails>>

    @GET("forecast.json")
    fun fetchForecastDetails(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("days") days: Int,
    ) : Flow<ApiResponse<ForecastDetails>>
}