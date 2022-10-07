package com.ahoy.weatherapp.model

import com.ahoy.weatherapp.R

data class Favourite (
    var isFavourite: Boolean = false,
    var isFavouriteString: String = isFavourite.toString(),
    var favIcon: Int = if (isFavourite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
)