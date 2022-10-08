package com.ahoy.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey


@Entity(tableName = "current_weather_details")
data class CurrentWeatherDetailsOffline (
    @PrimaryKey
    val city: String,
    var status: String,
    var currentTemperature: Double,
    var feelsLike: Double,
    var wind: Double,
    var humidity: Int,
    var icon: String,
)
