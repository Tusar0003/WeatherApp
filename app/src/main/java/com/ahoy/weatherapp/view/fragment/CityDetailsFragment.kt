package com.ahoy.weatherapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ahoy.weatherapp.R
import com.ahoy.weatherapp.databinding.FragmentCityDetailsBinding
import com.ahoy.weatherapp.db.entity.FavouriteCity
import com.ahoy.weatherapp.model.CurrentWeatherDetails
import com.ahoy.weatherapp.utils.launchAndRepeatWithViewLifecycle
import com.ahoy.weatherapp.utils.navigateUp
import com.ahoy.weatherapp.viewmodel.FavouriteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class CityDetailsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCityDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_city_details,
            container,
            false
        )

        arguments.let {
            it?.getSerializable("weatherDetails").let { weatherDetails ->
                binding.weatherDetails = weatherDetails as CurrentWeatherDetails?
            }
        }

        binding.cancelImageView.setOnClickListener {
            navigateUp()
        }

        return binding.root
    }
}
