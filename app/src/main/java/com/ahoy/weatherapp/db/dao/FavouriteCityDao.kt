package com.ahoy.weatherapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.ahoy.weatherapp.db.entity.FavouriteCity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCityDao: BaseDao<FavouriteCity> {
    @Query("SELECT * FROM favourite_city;")
    fun getFavouriteCityList(): Flow<List<FavouriteCity>>
}