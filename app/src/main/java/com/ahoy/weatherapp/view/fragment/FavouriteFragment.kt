package com.ahoy.weatherapp.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ahoy.weatherapp.R
import com.ahoy.weatherapp.databinding.FragmentFavouriteBinding
import com.ahoy.weatherapp.utils.*
import com.ahoy.weatherapp.viewmodel.FavouriteNavigationAction
import com.ahoy.weatherapp.viewmodel.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>(R.layout.fragment_favourite) {

    private val viewModel: FavouriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        launchAndRepeatWithViewLifecycle {
            launch {
                binding.toolbar.setNavigationOnClickListener {
                    navigateUp()
                }
            }

            launch {
                viewModel.message.collect {
                    showDialog {
                        setMessage(it)
                        positiveButton(getString(R.string.ok)) {}
                    }
                }
            }

            launch {
                viewModel.getFavouriteCity()
            }

            launch {
                viewModel.navigationActions.collect {
                    if (it == FavouriteNavigationAction.NavigateToDetailsAction) {
                        navigate(
                            FavouriteFragmentDirections.actionFavouriteFragmentToCityDetailsFragment(
                                weatherDetails = viewModel.weatherDetails.value
                            )
                        )
                    }
                }
            }
        }
    }
}
