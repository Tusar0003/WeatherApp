package com.ahoy.weatherapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastDetails(
    @Json(name = "forecast")
    var forecast: Forecast? = Forecast(),
)

data class Forecast(
    @Json(name = "forecastday")
    var forecastday: List<Forecastday> = arrayListOf()
)

data class Forecastday(
    @Json(name = "date")
    var date: String? = null,
    @Json(name = "date_epoch")
    var dateEpoch: Int? = null,
    @Json(name = "day")
    var day: Day? = null,
    @Json(name = "astro")
    var astro: Astro? = null,
    @Json(name = "hour")
    var hour: List<Hour>? = arrayListOf(),
)

data class Day(
    @Json(name = "maxtemp_c")
    var maxtempC: Double? = null,
    @Json(name = "maxtemp_f")
    var maxtempF: Double? = null,
    @Json(name = "mintemp_c")
    var mintempC: Double? = null,
    @Json(name = "mintemp_f")
    var mintempF: Double? = null,
    @Json(name = "avgtemp_c")
    var avgtempC: Double? = null,
    @Json(name = "avgtemp_f")
    var avgtempF: Double? = null,
    @Json(name = "condition")
    var condition: Condition? = Condition(),
)

data class Astro(
    @Json(name = "sunrise")
    var sunrise: String? = null,
    @Json(name = "sunset")
    var sunset: String? = null,
    @Json(name = "moonrise")
    var moonrise: String? = null,
    @Json(name = "moonset")
    var moonset: String? = null,
)

data class Hour(
    @Json(name = "time")
    var time: String? = null,
    @Json(name = "temp_c")
    var tempC: Double? = null,
    @Json(name = "temp_f")
    var tempF: Double? = null,
    @Json(name = "is_day")
    var isDay: Int? = null,
    @Json(name = "condition")
    var condition: Condition? = Condition(),
)

data class ForecastDetailsPayload(
    val query: String,
    val days: Int
)

