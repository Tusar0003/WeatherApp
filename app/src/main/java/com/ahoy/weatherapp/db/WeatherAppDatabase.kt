package com.ahoy.weatherapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahoy.weatherapp.db.dao.FavouriteCityDao
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.utils.Constants
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [
        FavouriteCity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherAppDatabase : RoomDatabase() {

    abstract val favouriteCityDao: FavouriteCityDao

    companion object {
        private const val databaseName = "weather_app_db"

        fun buildDatabase(context: Context): WeatherAppDatabase {
            val key = Constants.DATABASE_ENCRYPTION_KEY.toCharArray()
            val passphrase = SQLiteDatabase.getBytes(key)
            val supportFactory = SupportFactory(passphrase)
            return Room
                .databaseBuilder(context, WeatherAppDatabase::class.java, databaseName)
                .openHelperFactory(supportFactory)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
