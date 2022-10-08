package com.ahoy.weatherapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable


@JsonClass(generateAdapter = true)
data class CurrentWeatherDetails(
    @Json(name = "location")
    var location: Location? = Location(),
    @Json(name = "current")
    var current: Current? = Current(),
) : Serializable

data class Location(
    @Json(name = "name")
    var name: String? = null,
    @Json(name = "region")
    var region: String? = null,
    @Json(name = "country")
    var country: String? = null,
    @Json(name = "lat")
    var lat: Double? = null,
    @Json(name = "lon")
    var lon: Double? = null,
    @Json(name = "tz_id")
    var tzId: String? = null,
    @Json(name = "localtime_epoch")
    var localtimeEpoch: Int? = null,
    @Json(name = "localtime")
    var localtime: String? = null
)

data class Condition(
    @Json(name = "text")
    var text: String? = null,
    @Json(name = "icon")
    var icon: String? = null,
    @Json(name = "code")
    var code: Int? = null
)

data class Current(
    @Json(name = "last_updated_epoch")
    var lastUpdatedEpoch: Int? = null,
    @Json(name = "last_updated")
    var lastUpdated: String? = null,
    @Json(name = "temp_c")
    var tempC: Double? = null,
    @Json(name = "temp_f")
    var tempF: Double? = null,
    @Json(name = "is_day")
    var isDay: Int? = null,
    @Json(name = "condition")
    var condition: Condition? = Condition(),
    @Json(name = "wind_mph")
    var windMph: Double? = null,
    @Json(name = "wind_kph")
    var windKph: Double? = null,
    @Json(name = "wind_degree")
    var windDegree: Int? = null,
    @Json(name = "wind_dir")
    var windDir: String? = null,
    @Json(name = "pressure_mb")
    var pressureMb: Int? = null,
    @Json(name = "pressure_in")
    var pressureIn: Double? = null,
    @Json(name = "precip_mm")
    var precipMm: Double? = null,
    @Json(name = "precip_in")
    var precipIn: Double? = null,
    @Json(name = "humidity")
    var humidity: Int? = null,
    @Json(name = "cloud")
    var cloud: Int? = null,
    @Json(name = "feelslike_c")
    var feelslikeC: Double? = null,
    @Json(name = "feelslike_f")
    var feelslikeF: Double? = null,
    @Json(name = "vis_km")
    var visKm: Int? = null,
    @Json(name = "vis_miles")
    var visMiles: Int? = null,
    @Json(name = "uv")
    var uv: Int? = null,
    @Json(name = "gust_mph")
    var gustMph: Double? = null,
    @Json(name = "gust_kph")
    var gustKph: Double? = null
)
