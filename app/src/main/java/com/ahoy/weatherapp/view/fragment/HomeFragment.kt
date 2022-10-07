package com.ahoy.weatherapp.view.fragment

import android.Manifest
import android.app.Activity
import android.app.Application
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import com.ahoy.weatherapp.R
import com.ahoy.weatherapp.WeatherApp
import com.ahoy.weatherapp.databinding.FragmentHomeBinding
import com.ahoy.weatherapp.utils.*
import com.ahoy.weatherapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    R.layout.fragment_home
), LocationUtils.GpsListener {

    private val viewModel: HomeViewModel by viewModels()
    private var locationUtils: LocationUtils? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        locationUtils = LocationUtils(requireActivity())

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.message.collect {
                    showDialog {
                        setMessage(it)
                        positiveButton(getString(R.string.ok)) {}
                    }
                }
            }

            launch {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // we need to tell user why do we need permission
                    showDialog {
                        setMessage(R.string.location_rationale_text)
                        positiveButton(getString(R.string.ok)) {
                            locationPermission.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                        negativeButton(getString(R.string.no))
                    }
                } else {
                    locationPermission.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }

            launch {
                binding.toolbar.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_search -> {
                            navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                        }
                    }

                    return@setOnMenuItemClickListener true
                }
            }
        }
    }

    // Location implementation
    private val locationPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions  ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                // Precise location access granted.
                locationUtils?.trackLocation(this)
            }

            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                // Only approximate location access granted.
                locationUtils?.trackLocation(this)
            }

            !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> { //READ https://developer.android.com/about/versions/11/privacy/permissions
                // user denied permission and set Don't ask again.
                showDialog {
                    setMessage(R.string.location_dont_ask_text)
                    positiveButton(getString(R.string.ok)) {
                        openSettings()
                    }
                    negativeButton(getString(R.string.no))
                }
            }

            !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> { //READ https://developer.android.com/about/versions/11/privacy/permissions
                // user denied permission and set Don't ask again.
                showDialog {
                    setMessage(R.string.location_dont_ask_text)
                    positiveButton(getString(R.string.ok)) {
                        openSettings()
                    }
                    negativeButton(getString(R.string.no))
                }
            }
            else -> {
                requireContext().toastLong(R.string.denied_toast)
            }
        }
    }

    override fun onRequestGPSSetting(intentSenderRequest: IntentSenderRequest) {
        gpsSettingLauncher.launch(intentSenderRequest)
    }

    override fun onLocationTrackingInProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onGPSNotAvailable(exception: Exception?) {
        showDialog {
            setMessage(R.string.gps_not_available_text)
            positiveButton(getString(R.string.ok)) {}
        }
    }

    override fun onLocationPermissionNotAvailable() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onLocationTrackingFailed(exception: Exception?) {
        binding.progressBar.visibility = View.GONE
        showDialog {
            setMessage(R.string.location_tracking_failed_text)
            positiveButton(getString(R.string.ok)) {}
        }
    }

    override fun onLocationFound(location: Location) {
        binding.progressBar.visibility = View.GONE
        viewModel.setCurrentLatLng("${location.longitude}, ${location.latitude}")
//        requireContext().toastLong(R.string.location_tracking_success_text)
    }

    private val gpsSettingLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            locationUtils?.trackLocation(this)
        } else {
            showDialog {
                setMessage(R.string.gps_not_available_text)
                positiveButton(getString(R.string.ok)) {}
            }
        }
    }
}
