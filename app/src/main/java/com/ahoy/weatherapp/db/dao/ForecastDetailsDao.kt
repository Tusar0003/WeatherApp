package com.ahoy.weatherapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.ahoy.weatherapp.db.entity.ForecastDetailsOffline
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDetailsDao: BaseDao<ForecastDetailsOffline> {
    @Query("SELECT * FROM forecast_details;")
    fun getWeatherDetails(): Flow<List<ForecastDetailsOffline?>>
}
