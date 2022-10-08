package com.ahoy.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey


@Entity(tableName = "forecast_details")
data class ForecastDetailsOffline (
    @PrimaryKey
    val date: String,
    var averageTemperature: Double,
    var icon: String,
)
