package com.ahoy.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favourite_city")
data class FavouriteCity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var city: String,
    var country: String,
    var lat: Double,
    var lon: Double,
)
