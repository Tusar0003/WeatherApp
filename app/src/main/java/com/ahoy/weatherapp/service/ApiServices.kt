package com.ahoy.weatherapp.service

import com.ahoy.weatherapp.utils.ApiResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiServices {

    @POST("authentication")
    fun userAuthentication(@Body jsonObject: JsonObject) : Flow<ApiResponse<JsonObject>>
}