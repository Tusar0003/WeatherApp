package com.ahoy.weatherapp.model

import android.graphics.drawable.Drawable
import com.ahoy.weatherapp.R
import com.squareup.moshi.Json

data class Search(
    @Json(name = "id")
    var id: Int? = null,
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
    @Json(name = "url")
    var url: String? = null,
    var isFavourite: Boolean = false,
    var favIcon: Int = if (isFavourite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
)
