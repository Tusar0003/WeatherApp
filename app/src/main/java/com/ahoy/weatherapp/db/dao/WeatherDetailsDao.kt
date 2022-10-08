package com.ahoy.weatherapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.ahoy.weatherapp.db.entity.CurrentWeatherDetailsOffline
import com.ahoy.weatherapp.db.entity.FavouriteCity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDetailsDao: BaseDao<CurrentWeatherDetailsOffline> {
    @Query("SELECT * FROM current_weather_details;")
    fun getWeatherDetails(): Flow<CurrentWeatherDetailsOffline?>
}
